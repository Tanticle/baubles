package tld.unknown.baubles.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.ItemCapability;

public final class BaublesData {

    public static final String MOD_ID = "baubles";

    public static final class CapabilitiesAttachments {

        public static final ResourceLocation ID_ATTACHMENT_BAUBLES = id("baubles");
        public static final ResourceLocation ID_CAPABILITY_BAUBLE = id("bauble");

        public static final ItemCapability<IBauble, Void> CAPABILITY_BAUBLE = ItemCapability.createVoid(ID_CAPABILITY_BAUBLE, IBauble.class);
    }

    public static final class Tags {

        public static final TagKey<Item> ITEM_AMULET = item("amulet");
        public static final TagKey<Item> ITEM_RING = item("ring");
        public static final TagKey<Item> ITEM_BELT = item("belt");
        public static final TagKey<Item> ITEM_HEAD = item("head");
        public static final TagKey<Item> ITEM_BODY = item("body");
        public static final TagKey<Item> ITEM_CHARM = item("charm");
        public static final TagKey<Item> ITEM_TRINKET = item("any");

        private static TagKey<Item> item(String path) {
            return TagKey.create(Registries.ITEM, id(path));
        }
    }

    public static final class Textures {

        public static final ResourceLocation PLACEHOLDER_AMULET = id("item/slot_amulet");
        public static final ResourceLocation PLACEHOLDER_BELT = id("item/slot_belt");
        public static final ResourceLocation PLACEHOLDER_CHARM = id("item/slot_charm");
        public static final ResourceLocation PLACEHOLDER_CHEST = id("item/slot_chest");
        public static final ResourceLocation PLACEHOLDER_HEAD = id("item/slot_head");
        public static final ResourceLocation PLACEHOLDER_RING = id("item/slot_ring");

        public static final ResourceLocation UI_EXPANDED_INV = id("textures/gui/expanded_inventory.png");
        public static final ResourceLocation UI_BAUBLES_BUTTON = id("textures/gui/baubles_button.png");
    }

    public static final class Networking {

        public static final String VERSION = "1.0.0";

        public static final ResourceLocation SYNC_DATA = id("sync_data");
        public static final ResourceLocation OPEN_INV = id("open_inv");
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
