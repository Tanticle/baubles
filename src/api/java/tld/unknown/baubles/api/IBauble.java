package tld.unknown.baubles.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IBauble {

    default void onWornTick(BaubleType type, ItemStack stack, Player player) { }

    default void onEquipped(BaubleType type, ItemStack stack, Player player) { }

    default void onUnequipped(BaubleType type, ItemStack stack, Player player) { }

    default boolean canEquip(BaubleType type, ItemStack stack, Player player) { return true; }

    default boolean canUnequip(BaubleType type, ItemStack stack, Player player) { return true; }
}
