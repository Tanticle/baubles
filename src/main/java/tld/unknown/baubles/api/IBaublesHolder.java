package tld.unknown.baubles.api;

import net.minecraft.world.item.ItemStack;

public interface IBaublesHolder {

    int INVENTORY_SIZE = 7;

    ItemStack getBaubleInSlot(BaubleType type);

    boolean setBaubleInSlot(BaubleType type, ItemStack stack);

    ItemStack[] getAllSlots();
}
