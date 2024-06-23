package tld.unknown.baubles.api;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The main API Class for Baubles 2.
 * @see <a href=https://github.com/Tanticle/baubles/wiki>Baubles 2 Wiki</a>
 * @author Tom Tanticle
 */
public final class BaublesAPI {

    /**
     * Whether the {@link ItemStack} has an {@link IBauble} implementation via the interface or capability.
     * @param stack The {@link ItemStack} to check.
     * @return Whether the {@link ItemStack} has an {@link IBauble} implementation.
     */
    public static boolean hasBaubleImplementation(@NotNull ItemStack stack) {
        return getBaubleImplementation(stack) != null;
    }

    /**
     * Gets the {@link IBauble} implementation present on this {@link ItemStack}, or null when neither the item nor the capability provide one.
     * @param stack The {@link ItemStack} to check.
     * @return The {@link IBauble} implementation, or null when none is present.
     */
    public static @Nullable IBauble getBaubleImplementation(@NotNull ItemStack stack) {
        return stack.getItem() instanceof IBauble b ? b : stack.getCapability(BaublesData.CapabilitiesAttachments.CAPABILITY_BAUBLE);
    }

    /**
     *  Whether this {@link ItemStack} can be put into any bauble slot, regardless of implementation.
     * @param stack The {@link ItemStack} to check.
     * @return Whether this {@link ItemStack} is valid in any bauble slot.
     */
    public static boolean isBaubleItem(@NotNull ItemStack stack) {
        return hasBaubleImplementation(stack) || BaubleType.hasBaubleTags(stack);
    }

    /**
     * Collects a list of valid {@link BaubleType} values for this {@link ItemStack}, can be empty when no slots are valid.
     * @param stack The {@link ItemStack} to check.
     * @return A (potentially empty) list of all valid {@link BaubleType} values.
     */
    public static List<BaubleType> getBaubleTypes(@NotNull ItemStack stack) {
        List<BaubleType> set = new ArrayList<>();
        if(!isBaubleItem(stack))
            return set;
        for(BaubleType baubleType : BaubleType.values())
            if(baubleType.isItemValid(stack))
                set.add(baubleType);
        return set;
    }

    /**
     * Returns an {@link IBaubleRenderers} implementation to register {@link IBaubleRenderer} implementations.
     * @return An {@link IBaubleRenderers} implementation.
     */
    public static IBaubleRenderers getRenderers() {
        return renderers;
    }

    private static IBaubleRenderers renderers;

    @ApiStatus.Internal
    public static void setRenderHandlers(IBaubleRenderers renderers) {
        BaublesAPI.renderers = renderers;
    }
}
