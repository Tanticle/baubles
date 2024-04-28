package tld.unknown.baubles.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum BaubleType {
    AMULET(BaublesData.Tags.ITEM_AMULET, BaublesData.Textures.PLACEHOLDER_AMULET, Component.translatable("name.baubles.amulet")),
    RING_RIGHT(BaublesData.Tags.ITEM_RING, BaublesData.Textures.PLACEHOLDER_RING, Component.translatable("name.baubles.ring")),
    RING_LEFT(BaublesData.Tags.ITEM_RING, BaublesData.Textures.PLACEHOLDER_RING, Component.translatable("name.baubles.ring")),
    BELT(BaublesData.Tags.ITEM_BELT, BaublesData.Textures.PLACEHOLDER_BELT, Component.translatable("name.baubles.belt")),
    HEAD(BaublesData.Tags.ITEM_HEAD, BaublesData.Textures.PLACEHOLDER_HEAD, Component.translatable("name.baubles.head")),
    BODY(BaublesData.Tags.ITEM_BODY, BaublesData.Textures.PLACEHOLDER_CHEST, Component.translatable("name.baubles.chest")),
    CHARM(BaublesData.Tags.ITEM_CHARM, BaublesData.Textures.PLACEHOLDER_CHARM, Component.translatable("name.baubles.charm"));

    public static final Component NAME_PREFIX = Component.translatable("name.baubles.bauble").withStyle(ChatFormatting.GOLD).append(" - ");

    private final TagKey<Item> slotTag;
    private final ResourceLocation placeholderTexture;
    private final Component name;

    BaubleType(TagKey<Item> slotTag, ResourceLocation placeholderTexture, Component name) {
        this.slotTag = slotTag;
        this.placeholderTexture = placeholderTexture;
        this.name = name;
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
        return stack == ItemStack.EMPTY || (BaublesAPI.isBaubleItem(stack) && (stack.is(this.slotTag) || stack.is(BaublesData.Tags.ITEM_TRINKET)));
    }

    public Component getDisplayName() {
        return NAME_PREFIX.copy().append(this.name);
    }
}
