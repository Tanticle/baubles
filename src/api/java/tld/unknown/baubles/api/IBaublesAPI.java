package tld.unknown.baubles.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * The main API Class for Baubles 2.
 * @see <a href=https://github.com/Tanticle/baubles/wiki>Baubles 2 Wiki</a>
 * @author Tom Tanticle
 */
public interface IBaublesAPI {

    /**
     * Whether the {@link ItemStack} has an {@link IBauble} implementation via the interface or capability.
     * @param stack The {@link ItemStack} to check.
     * @return Whether the {@link ItemStack} has an {@link IBauble} implementation.
     */
    boolean hasBaubleImplementation(@NotNull ItemStack stack);

    /**
     * Gets the {@link IBauble} implementation present on this {@link ItemStack}, or null when neither the item nor the capability provide one.
     * @param stack The {@link ItemStack} to check.
     * @return The {@link IBauble} implementation, or null when none is present.
     */
    @Nullable IBauble getBaubleImplementation(@NotNull ItemStack stack);

    /**
     *  Whether this {@link ItemStack} can be put into any bauble slot, regardless of implementation.
     * @param stack The {@link ItemStack} to check.
     * @return Whether this {@link ItemStack} is valid in any bauble slot.
     */
    boolean isBaubleItem(@NotNull ItemStack stack);

    /**
     * Collects a list of valid {@link BaubleType} values for this {@link ItemStack}, can be empty when no slots are valid.
     * @param stack The {@link ItemStack} to check.
     * @return A (potentially empty) list of all valid {@link BaubleType} values.
     */
    List<BaubleType> getBaubleTypes(@NotNull ItemStack stack);

	/**
	 * Returns the {@link IBaublesHolder} for the given {@link Player}.
	 * @param player The {@link Player} being queried for their {@link IBaublesHolder}.
	 */
	 IBaublesHolder getBaublesInventory(@NotNull Player player);

	@ApiStatus.Internal
	Map<ResourceLocation, IBaubleRenderer> getBaubleRenderers();
}
