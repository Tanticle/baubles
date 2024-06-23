package tld.unknown.baubles.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * The main interface for defining bauble behaviour.
 * @see <a href=https://github.com/Tanticle/baubles/wiki/Creating-a-Bauble>Baubles 2 Wiki on implementing a Bauble</a>
 * @author Tom Tanticle
 */
public interface IBauble {

    /**
     * Called every tick when this {@link ItemStack} is present in the given {@link IBaublesHolder} slot.
     * @param type The {@link BaubleType} of the given {@link IBaublesHolder} slot.
     * @param stack The currently evaluated {@link ItemStack}.
     * @param player The {@link Player} currently being evaluated.
     */
    default void onWornTick(BaubleType type, ItemStack stack, Player player) { }

    /**
     * Called when this {@link ItemStack} is put into the given {@link IBaublesHolder} slot.
     * @param type The {@link BaubleType} of the given {@link IBaublesHolder} slot.
     * @param stack The currently evaluated {@link ItemStack}.
     * @param player The {@link Player} currently being evaluated.
     */
    default void onEquipped(BaubleType type, ItemStack stack, Player player) { }

    /**
     * Called when this {@link ItemStack} is removed from the given {@link IBaublesHolder} slot.
     * @param type The {@link BaubleType} of the given {@link IBaublesHolder} slot.
     * @param stack The currently evaluated {@link ItemStack}.
     * @param player The {@link Player} currently being evaluated.
     */
    default void onUnequipped(BaubleType type, ItemStack stack, Player player) { }

    /**
     * Whether this {@link ItemStack} can be put into the given {@link IBaublesHolder} slot.
     * @param type The {@link BaubleType} of the given {@link IBaublesHolder} slot.
     * @param stack The currently evaluated {@link ItemStack}.
     * @param player The {@link Player} currently being evaluated.
     * @return
     */
    default boolean canEquip(BaubleType type, ItemStack stack, Player player) { return true; }

    /**
     * Whether this {@link ItemStack} can be removed from the given {@link IBaublesHolder} slot.
     * @param type The {@link BaubleType} of the given {@link IBaublesHolder} slot.
     * @param stack The currently evaluated {@link ItemStack}.
     * @param player The {@link Player} currently being evaluated.
     * @return
     */
    default boolean canUnequip(BaubleType type, ItemStack stack, Player player) { return true; }
}
