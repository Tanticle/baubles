package tld.unknown.baubles.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum BaubleType {
    AMULET(BaublesData.Tags.ITEM_AMULET, BaublesData.Textures.PLACEHOLDER_AMULET),
    RING_RIGHT(BaublesData.Tags.ITEM_RING, BaublesData.Textures.PLACEHOLDER_RING),
    RING_LEFT(BaublesData.Tags.ITEM_RING, BaublesData.Textures.PLACEHOLDER_RING),
    BELT(BaublesData.Tags.ITEM_BELT, BaublesData.Textures.PLACEHOLDER_BELT),
    HEAD(BaublesData.Tags.ITEM_HEAD, BaublesData.Textures.PLACEHOLDER_HEAD),
    BODY(BaublesData.Tags.ITEM_BODY, BaublesData.Textures.PLACEHOLDER_CHEST),
    CHARM(BaublesData.Tags.ITEM_CHARM, BaublesData.Textures.PLACEHOLDER_CHARM);

    public static final Component NAME_PREFIX = Component.translatable("name.baubles.prefix").withStyle(ChatFormatting.GOLD);

    private final TagKey<Item> slotTag;
    private final ResourceLocation placeholderTexture;

    BaubleType(TagKey<Item> slotTag, ResourceLocation placeholderTexture) {
        this.slotTag = slotTag;
        this.placeholderTexture = placeholderTexture;
    }

    public static boolean hasBaubleTags(ItemStack stack) {
        if(stack.is(BaublesData.Tags.ITEM_TRINKET))
            return true;
        for (BaubleType type : BaubleType.values())
            if(stack.is(type.slotTag))
                return true;
        return false;
    }

    public static BaubleType bySlotId(int slotId) {
        for (BaubleType type : BaubleType.values()) {
            if(type.ordinal() == slotId)
                return type;
        }
        return null;
    }

    public ResourceLocation getPlaceholderTexture() {
        return this.placeholderTexture;
    }

    public boolean isItemValid(ItemStack stack) {
        return BaublesAPI.isBaubleItem(stack) && (stack.is(this.slotTag) || stack.is(BaublesData.Tags.ITEM_TRINKET));
    }

    public Component getDisplayName() {
        Component name = Component.translatable("name.baubles." + this.name().toLowerCase());
        return NAME_PREFIX.copy().append(name);
    }
}
