package tld.unknown.baubles_example;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.Baubles;

import java.util.List;

@Mod("baubles_example")
public class BaublesExample {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("baubles_example", "ring");
    private static final ResourceLocation ID_2 = ResourceLocation.fromNamespaceAndPath("baubles_example", "ring_2");
    private static final DeferredRegister.Items REGISTRY_ITEMS = DeferredRegister.Items.createItems(ID.getNamespace());

    public static final DeferredItem<TestRingItem> ITEM_RING = REGISTRY_ITEMS.registerItem(
            ID.getPath(),
            TestRingItem::new);

	public static final DeferredItem<Item> ITEM_RING_2 = REGISTRY_ITEMS.registerItem(
			ID_2.getPath(),
			p -> new Item(p.rarity(Rarity.RARE).component(Baubles.COMPONENT_BAUBLE, List.of(BaubleType.BODY, BaubleType.AMULET))));

    public BaublesExample(IEventBus modEventBus) {
        REGISTRY_ITEMS.register(modEventBus);
    }

    @EventBusSubscriber(modid = "baubles_example", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ModBusSubscriber {

        @SubscribeEvent
        public static void onClientInit(final FMLClientSetupEvent event) {
			Baubles.API.getRenderers().registerRenderer(ID, new TestRingItem.TestRenderer());
			Baubles.API.getRenderers().registerRenderer(ID_2, new TestRingItem.TestRenderer());
        }
    }
}
