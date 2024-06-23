package tld.unknown.baubles.api;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The data attachment interface for the {@link net.minecraft.world.entity.player.Player}.
 * @author Tom Tanticle
 */
public interface IBaublesHolder {

    /**
     * The max size of the baubles inventory.
     */
    int INVENTORY_SIZE = 7;

    /**
     * Gets the current {@link ItemStack} in the given {@link IBaublesHolder} slot {@link BaubleType}.
     * @param type The {@link IBaublesHolder} slot {@link BaubleType}.
     * @return The {@link ItemStack} in the given {@link IBaublesHolder} slot {@link BaubleType}. Can be an empty {@link ItemStack}.
     */
    ItemStack getBaubleInSlot(@NotNull BaubleType type);

    /**
     * Whether the given {@link ItemStack} can be put into the defined {@link IBaublesHolder} slot {@link BaubleType}.
     * @param type The given {@link IBaublesHolder} slot {@link BaubleType}.
     * @param stack The {@link ItemStack} to be tested.
     * @return Whether the given {@link ItemStack} is valid for this {@link IBaublesHolder} slot {@link BaubleType}.
     */
    boolean setBaubleInSlot(@NotNull BaubleType type, @NotNull ItemStack stack);

    /**
     * Returns an array of all {@link ItemStack} entries in this {@link IBaublesHolder}, following the ordinal order of {@link BaubleType}.
     * @return An array containing all {@link ItemStack} entries in this {@link IBaublesHolder}, following the ordinal order of {@link BaubleType}
     */
    ItemStack[] getAllSlots();
}
