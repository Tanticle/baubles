package tld.unknown.baubles.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.ApiStatus;

/**
 * A static utility class providing all sorts of constants and values for Baubles 2
 * @author Tom Tanticle
 */
public final class BaublesData {

    /**
     * The mod id for Baubles 2.
     */
    public static final String MOD_ID = "baubles";

    /**
     * The current API version. As long as the API version matches, mismatching versions of the mod will work.
     */
    public static final String API_VERSION = "pre_1.0";

    /**
     * {@link ResourceLocation} ids and {@link ItemCapability} definitions for Baubles 2.
     */
    public static final class CapabilitiesAttachments {

        public static final ResourceLocation ID_ATTACHMENT_BAUBLES = id("baubles");
        public static final ResourceLocation ID_CAPABILITY_BAUBLE = id("bauble");

        public static final ItemCapability<IBauble, Void> CAPABILITY_BAUBLE = ItemCapability.createVoid(ID_CAPABILITY_BAUBLE, IBauble.class);
    }

    /**
     * Slot assignment {@link TagKey} definitions for Baubles 2.
     * @see <a href=https://github.com/Tanticle/baubles/wiki/Datapack-Configuration>Baubles 2 Wiki on Datapack Configuration</a>
     */
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

    /**
     * Texture path constants as {@link ResourceLocation} for Baubles 2.
     */
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

    /**
     * Network Packet ids as {@link ResourceLocation} for Baubles 2.
     */
    public static final class Networking {

        public static final ResourceLocation SYNC_DATA = id("sync_data");
        public static final ResourceLocation OPEN_INV = id("open_inv");
    }

    @ApiStatus.Internal
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
