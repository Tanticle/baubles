package tld.unknown.baubles.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An enum representing the 7 slots present in Baubles 2.
 * @author Tom Tanticle
 */
public enum BaubleType {

    AMULET(BaublesData.Tags.ITEM_AMULET, BaublesData.Textures.PLACEHOLDER_AMULET, Component.translatable("name.baubles.amulet")),
    RING_RIGHT(BaublesData.Tags.ITEM_RING, BaublesData.Textures.PLACEHOLDER_RING, Component.translatable("name.baubles.ring")),
    RING_LEFT(BaublesData.Tags.ITEM_RING, BaublesData.Textures.PLACEHOLDER_RING, Component.translatable("name.baubles.ring")),
    BELT(BaublesData.Tags.ITEM_BELT, BaublesData.Textures.PLACEHOLDER_BELT, Component.translatable("name.baubles.belt")),
    HEAD(BaublesData.Tags.ITEM_HEAD, BaublesData.Textures.PLACEHOLDER_HEAD, Component.translatable("name.baubles.head")),
    BODY(BaublesData.Tags.ITEM_BODY, BaublesData.Textures.PLACEHOLDER_CHEST, Component.translatable("name.baubles.body")),
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

    /**
     * Whether the given {@link ItemStack} has any {@link TagKey} assignments that marks it as a bauble slot compatible item.
     * @param stack The {@link ItemStack} to check.
     * @return Whether the given {@link ItemStack} has any valid {@link TagKey} assignments.
     */
    public static boolean hasBaubleTags(@NotNull ItemStack stack) {
        if(stack.is(BaublesData.Tags.ITEM_TRINKET))
            return true;
        for (BaubleType type : BaubleType.values())
            if(stack.is(type.slotTag))
                return true;
        return false;
    }

    /**
     * Returns the {@link BaubleType} slot from the provided {@link IBaublesHolder} slot id. May be null if out of bounds.
     * @param slotId The enum constant ordinal based on the {@link IBaublesHolder} slot id.
     * @return The {@link BaubleType} corresponding to the {@link IBaublesHolder} slot id, or null if out of bounds.
     */
    public static @Nullable BaubleType bySlotId(@NonNegative int slotId) {
        for (BaubleType type : BaubleType.values()) {
            if(type.ordinal() == slotId)
                return type;
        }
        return null;
    }

    /**
     * Returns a {@link ResourceLocation} to the placeholder texture of this {@link BaubleType} slot.
     * @return The {@link ResourceLocation} of the texture.
     */
    public ResourceLocation getPlaceholderTexture() {
        return this.placeholderTexture;
    }

    /**
     * Whether the given {@link ItemStack} is valid for this {@link BaubleType}
     * @param stack The {@link ItemStack} to check.
     * @return Whether the given {@link ItemStack} is valid.
     */
    public boolean isItemValid(@NotNull ItemStack stack) {
        return stack == ItemStack.EMPTY || (BaublesAPI.isBaubleItem(stack) && (stack.is(this.slotTag) || stack.is(BaublesData.Tags.ITEM_TRINKET)));
    }

    /**
     * Returns a pre-formatted {@link Component} for this {@link BaubleType}.
     * @return The pre-formatted {@link Component}.
     */
    public Component getDisplayName() {
        return NAME_PREFIX.copy().append(this.name);
    }
}
