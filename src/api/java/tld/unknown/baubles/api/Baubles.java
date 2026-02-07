package tld.unknown.baubles.api;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * A static utility class providing all sorts of constants and values for Baubles 2.
 * @author Tom Tanticle
 */
public final class Baubles {

	/**
	 * The main {@link IBaublesAPI} instance.
	 */
	public static IBaublesAPI API;

    /**
     * The mod id for Baubles 2.
     */
    public static final String MOD_ID = "baubles";

    /**
     * The current API version. As long as the API version matches, mismatching versions of the mod will work.
     */
    public static final String API_VERSION = "pre_1.0";

	/**
	 * {@link ResourceLocation} that identifies the {@link IBaublesHolder} attachment.
	 */
	public static final ResourceLocation ID_ATTACHMENT_BAUBLES = id("baubles");

	/**
	 * {@link ResourceLocation} that identifies the {@link ItemCapability} for items.
	 */
	public static final ItemCapability<IBauble, Void> CAPABILITY_BAUBLE = ItemCapability.createVoid(id("bauble"), IBauble.class);

	/**
	 * {@link DataComponentType} used to create an {@link ItemStack} equipable in the given {@link BaubleType}.
	 */
	public static final DataComponentType<List<BaubleType>> COMPONENT_BAUBLE = DataComponentType.<List<BaubleType>>builder()
			.persistent(enumCodec(BaubleType.class).listOf())
			.networkSynchronized(ByteBufCodecs.<ByteBuf, BaubleType>list().apply(enumStreamCodec(BaubleType.class)))
			.build();

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

        public static final ResourceLocation PLACEHOLDER_AMULET = id("container/slot/amulet");
        public static final ResourceLocation PLACEHOLDER_BELT = id("container/slot/belt");
        public static final ResourceLocation PLACEHOLDER_CHARM = id("container/slot/charm");
        public static final ResourceLocation PLACEHOLDER_CHEST = id("container/slot/chest");
        public static final ResourceLocation PLACEHOLDER_HEAD = id("container/slot/head");
        public static final ResourceLocation PLACEHOLDER_RING = id("container/slot/ring");

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

	@ApiStatus.Internal
	private static <E extends Enum<E>> Codec<E> enumCodec(Class<E> clazz) {
		return Codec.INT.xmap(i -> clazz.getEnumConstants()[i], Enum::ordinal);
	}

	@ApiStatus.Internal
	private static <E extends Enum<E>> StreamCodec<ByteBuf, E> enumStreamCodec(Class<E> clazz) {
		return new StreamCodec<>() {
			public E decode(ByteBuf buffer) { return clazz.getEnumConstants()[buffer.readInt()]; }
			public void encode(ByteBuf buffer, E value) { buffer.writeInt(value.ordinal()); }
		};
	}
}
