package tld.unknown.baubles.client;

import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import tld.unknown.baubles.BaublesMod;
import tld.unknown.baubles.api.BaubleRenderContext;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.Baubles;
import tld.unknown.baubles.api.IBaubleRenderer;
import tld.unknown.baubles.client.gui.BaublesButton;
import tld.unknown.baubles.client.gui.ExpandedInventoryScreen;
import tld.unknown.baubles.client.rendering.BaublesRenderLayer;
import tld.unknown.baubles.menu.ExpandedInventoryMenu;
import tld.unknown.baubles.networking.ServerboundOpenBaublesInvPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public final class ClientEventHandlers {

    @EventBusSubscriber(modid = Baubles.MOD_ID, value = Dist.CLIENT)
    public static final class ModBusSubscriber {

        @SubscribeEvent
        public static void registerKeybinds(final RegisterKeyMappingsEvent event) {
            event.register(BaublesClient.KEY_INVENTORY);
            if(!FMLLoader.getCurrent().isProduction())
                event.register(BaublesClient.KEY_DEBUG);
        }

        @SubscribeEvent
        public static void registerScreens(final RegisterMenuScreensEvent event) {
            event.register(BaublesMod.MENU_EXPANDED_INVENTORY.get(), new MenuScreens.ScreenConstructor<ExpandedInventoryMenu, ExpandedInventoryScreen>() {
				@Override
				public ExpandedInventoryScreen create(ExpandedInventoryMenu menu, Inventory inventory, Component title) {
					return new ExpandedInventoryScreen(menu, inventory.player, title);
				}
			});
        }

        @SubscribeEvent
        public static void layerRegistration(final EntityRenderersEvent.AddLayers event) {
            addLayerToHumanoid(event, EntityType.MANNEQUIN, BaublesRenderLayer::new);
            event.getSkins().forEach(s -> {
				var renderer = event.getPlayerRenderer(s);
                renderer.addLayer(new BaublesRenderLayer(renderer));
            });
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private static <E extends LivingEntity, S extends HumanoidRenderState, M extends HumanoidModel<S>>
        void addLayerToHumanoid(EntityRenderersEvent.AddLayers event, EntityType<E> entityType, Function<LivingEntityRenderer<E, S, M>, ? extends RenderLayer<S, M>> factory) {
            EntityRenderer<E, S> renderer = event.getRenderer(entityType);
            if (renderer instanceof LivingEntityRenderer ler) ler.addLayer(factory.apply(ler));
        }

        @SubscribeEvent
        public static void clientTick(final ClientTickEvent.Pre event) {
            if(BaublesClient.KEY_INVENTORY.consumeClick()) {
                Minecraft.getInstance().getConnection().send(new ServerboundOpenBaublesInvPacket());
            }
            if(BaublesClient.KEY_DEBUG.consumeClick()) {
                BaublesClient.RENDERERS.toggleRenderDebugMode();
            }
        }

        @SubscribeEvent
        public static void onRenderStateInject(final RegisterRenderStateModifiersEvent event) {
            event.registerEntityModifier(new TypeToken<AvatarRenderer<?>>() {}, (entity, state) -> {
                List<Pair<Identifier, ? extends BaubleRenderContext>> contexts = new ArrayList<>();
                ItemStack[] baubles = entity.getData(BaublesMod.ATTACHMENT_BAUBLES).getAllSlots();
                for (BaubleType slot : BaubleType.values()) {
                    ItemStack baubleItem = baubles[slot.ordinal()];
                    if(baubleItem == ItemStack.EMPTY || !Baubles.API.isBaubleItem(baubleItem))
                        continue;
                    Identifier itemId = BuiltInRegistries.ITEM.getKey(baubleItem.getItem());
                    IBaubleRenderer<? extends BaubleRenderContext> renderer = BaublesClient.RENDERERS.getRenderer(itemId);
                    if(renderer == null)
                        continue;
                    contexts.add(Pair.of(itemId, renderer.prepareRenderState(baubleItem, slot, (Avatar) entity, entity.level())));
                }
                state.setRenderData(Baubles.CONTEXT_BAUBLES, contexts);
            });
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
            if(Baubles.API.isBaubleItem(event.getItemStack())) {
                if(event.getItemStack().is(Baubles.Tags.ITEM_TRINKET)) {
                    event.getTooltipElements().add(Either.left(BaubleType.NAME_PREFIX.copy().append(Component.translatable("name.bauble.any"))));
                } else {
                    Baubles.API.getBaubleTypes(event.getItemStack()).forEach(t -> {
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
