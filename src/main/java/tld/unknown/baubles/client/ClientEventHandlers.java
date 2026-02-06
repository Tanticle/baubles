package tld.unknown.baubles.client;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.*;
import tld.unknown.baubles.Registries;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.BaublesAPI;
import tld.unknown.baubles.api.BaublesData;
import tld.unknown.baubles.client.gui.BaublesButton;
import tld.unknown.baubles.client.gui.ExpandedInventoryScreen;
import tld.unknown.baubles.client.rendering.BaubleRenderers;
import tld.unknown.baubles.client.rendering.BaublesRenderLayer;
import tld.unknown.baubles.menu.ExpandedInventoryMenu;
import tld.unknown.baubles.networking.ServerboundOpenBaublesInvPacket;


public final class ClientEventHandlers {

    @EventBusSubscriber(modid = BaublesData.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModBusSubscriber {

        @SubscribeEvent
        public static void registerKeybinds(final RegisterKeyMappingsEvent event) {
            event.register(BaublesClient.KEY_INVENTORY);
            if(!FMLLoader.isProduction())
                event.register(BaublesClient.KEY_DEBUG);
        }

        @SubscribeEvent
        public static void registerScreens(final RegisterMenuScreensEvent event) {
            event.register(Registries.MENU_EXPANDED_INVENTORY.get(), new MenuScreens.ScreenConstructor<ExpandedInventoryMenu, ExpandedInventoryScreen>() {
				@Override
				public ExpandedInventoryScreen create(ExpandedInventoryMenu menu, Inventory inventory, Component title) {
					return new ExpandedInventoryScreen(menu, inventory.player, title);
				}
			});
        }

        @SubscribeEvent
        public static void layerRegistration(final EntityRenderersEvent.AddLayers event) {
            event.getSkins().forEach(s -> {
				var renderer = event.getSkin(s);
				if (renderer instanceof LivingEntityRenderer ler)
					ler.addLayer(new BaublesRenderLayer(ler));
            });
        }
    }

    @EventBusSubscriber(modid = BaublesData.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static final class ForgeBusSubscriber {

        @SubscribeEvent
        public static void clientTick(final ClientTickEvent.Pre event) {
            if(BaublesClient.KEY_INVENTORY.consumeClick()) {
                Minecraft.getInstance().getConnection().send(new ServerboundOpenBaublesInvPacket(0, 0));
            }
            if(BaublesClient.KEY_DEBUG.consumeClick()) {
                ((BaubleRenderers)BaublesAPI.getRenderers()).toggleRenderDebugMode();
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
                        if(t == BaubleType.RING_LEFT || t == BaubleType.RING_RIGHT) {
                            if(!event.getTooltipElements().contains(Either.left(t.getDisplayName())))
                                event.getTooltipElements().add(Either.left(t.getDisplayName()));
                        } else
                            event.getTooltipElements().add(Either.left(t.getDisplayName()));
                    });
                }
            }
        }

        /*@SubscribeEvent
        public static void onArmRenderer(final RenderArmEvent event) {
            float width = pixelToUnit(4.5F);
            float height = pixelToUnit(12.5F);
            float downOffset = pixelToUnit(0.25F);
            VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.debugFilledBox());
            LevelRenderer.addChainedFilledBoxVertices(event.getPoseStack(), consumer, 0, 0, 0, 1, 1, 1, 1F, 1F, 1F, 0.5F);
        }

        /*private static float pixelToUnit(float pixels) {
            return 1F / 16 * pixels;
        }*/
    }
}
