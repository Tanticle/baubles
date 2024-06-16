package tld.unknown.baubles;

import net.minecraft.core.registries.BuiltInRegistries;
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

@Mod(BaublesData.MOD_ID)
public class BaublesExample {

    private static final DeferredRegister<Item> REGISTRY_ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, BaublesData.MOD_ID);

    public static final DeferredHolder<Item, TestRingItem> ITEM_RING = REGISTRY_ITEMS.register(
            "ring",
            TestRingItem::new);

    public BaublesExample(IEventBus modEventBus) {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        REGISTRY_ITEMS.register(modEventBus);
    }

    @EventBusSubscriber(modid = BaublesData.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModBusSubscriber {

        @SubscribeEvent
        public static void onClientInit(final FMLClientSetupEvent event) {
            BaublesAPI.getRenderers().registerRenderer(BaublesData.id("ring"), new TestRingItem.TestRenderer());
        }
    }
}
