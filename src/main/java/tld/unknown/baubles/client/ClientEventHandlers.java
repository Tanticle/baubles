package tld.unknown.baubles.client;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import tld.unknown.baubles.Registries;
import tld.unknown.baubles.TestRingItem;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.BaublesAPI;
import tld.unknown.baubles.api.BaublesData;
import tld.unknown.baubles.client.gui.BaublesButton;
import tld.unknown.baubles.client.gui.ExpandedInventoryScreen;
import tld.unknown.baubles.client.rendering.BaublesRenderLayer;
import tld.unknown.baubles.networking.ServerboundOpenInvPacket;


public final class ClientEventHandlers {

    @Mod.EventBusSubscriber(modid = BaublesData.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModBusSubscriber {

        @SubscribeEvent
        public static void registerKeybinds(final RegisterKeyMappingsEvent event) {
            event.register(BaublesClient.KEY_INVENTORY);
        }

        @SubscribeEvent
        public static void registerScreens(final RegisterMenuScreensEvent event) {
            event.register(Registries.MENU_EXPANDED_INVENTORY.get(), ExpandedInventoryScreen::new);
        }

        @SubscribeEvent
        public static void layerRegistration(final EntityRenderersEvent.AddLayers event) {
            event.getSkins().forEach(s -> {
                LivingEntityRenderer<Player, PlayerModel<Player>> r = event.getSkin(s);
                r.addLayer(new BaublesRenderLayer(r));
            });
        }

        @SubscribeEvent
        public static void onClientInit(final FMLClientSetupEvent event) {
            BaublesAPI.registerRenderer(BaublesData.id("ring"), new TestRingItem.TestRenderer());
        }
    }

    @Mod.EventBusSubscriber(modid = BaublesData.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static final class ForgeBusSubscriber {

        @SubscribeEvent
        public static void clientTick(final TickEvent.ClientTickEvent event) {
            if(event.phase == TickEvent.Phase.START) {
                if(BaublesClient.KEY_INVENTORY.consumeClick()) {
                    Minecraft.getInstance().getConnection().send(new ServerboundOpenInvPacket(true, 0, 0));
                }
            }
        }

        @SubscribeEvent
        public static void screenInitPost(final ScreenEvent.Init.Post event) {
            boolean isExpandedInv = event.getScreen() instanceof ExpandedInventoryScreen;
            if(event.getScreen() instanceof InventoryScreen || isExpandedInv) {
                event.addListener(new BaublesButton((AbstractContainerScreen<?>)event.getScreen(), 64, 9, isExpandedInv));
            }
        }

        @SubscribeEvent
        public static void onTooltipGather(final RenderTooltipEvent.GatherComponents event) {
            if(BaublesAPI.isBaubleItem(event.getItemStack())) {
                if(event.getItemStack().is(BaublesData.Tags.ITEM_TRINKET)) {
                    event.getTooltipElements().add(Either.left(BaubleType.NAME_PREFIX.copy().append(Component.translatable("name.bauble.any"))));
                } else {
                    BaublesAPI.getBaubleTypes(event.getItemStack()).forEach(t -> {
                        event.getTooltipElements().add(Either.left(t.getDisplayName()));
                    });
                }
            }
        }
    }
}
