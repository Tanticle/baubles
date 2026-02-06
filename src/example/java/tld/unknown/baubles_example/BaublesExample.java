package tld.unknown.baubles_example;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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

@Mod("baubles_example")
public class BaublesExample {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("baubles_example", "ring");
    private static final DeferredRegister.Items REGISTRY_ITEMS = DeferredRegister.Items.createItems(ID.getNamespace());

    public static final DeferredHolder<Item, TestRingItem> ITEM_RING = REGISTRY_ITEMS.registerItem(
            ID.getPath(),
            TestRingItem::new);

    public BaublesExample(IEventBus modEventBus) {
        REGISTRY_ITEMS.register(modEventBus);
    }

    @EventBusSubscriber(modid = "baubles_example", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModBusSubscriber {

        @SubscribeEvent
        public static void onClientInit(final FMLClientSetupEvent event) {
            BaublesAPI.getRenderers().registerRenderer(ID, new TestRingItem.TestRenderer());
        }
    }
}
