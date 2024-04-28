package tld.unknown.baubles_example;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.baubles.api.BaublesAPI;
import tld.unknown.baubles.api.BaublesData;

@Mod(BaublesExample.MOD_ID)
public class BaublesExample {

    public static final String MOD_ID = "baubles_example";

    private static final DeferredRegister<Item> REGISTRY_ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MOD_ID);

    public static final DeferredHolder<Item, TestRingItem> ITEM_RING = REGISTRY_ITEMS.register(
            "ring",
            TestRingItem::new);

    public BaublesExample(IEventBus modEventBus) {
        REGISTRY_ITEMS.register(modEventBus);
    }

    @EventBusSubscriber(modid = BaublesData.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModBusSubscriber {

        @SubscribeEvent
        public static void onClientInit(final FMLClientSetupEvent event) {
            BaublesAPI.registerRenderer(new ResourceLocation(MOD_ID, "ring"), new TestRingItem.TestRenderer());
        }
    }
}
