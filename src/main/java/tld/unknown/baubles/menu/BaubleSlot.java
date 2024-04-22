package tld.unknown.baubles.menu;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;
import tld.unknown.baubles.api.BaublesAPI;
import tld.unknown.baubles.BaublesInventoryCapability;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.IBauble;

public class BaubleSlot extends SlotItemHandler {

    private final BaubleType type;

    public BaubleSlot(BaublesInventoryCapability handler, int index, int xPosition, int yPosition) {
        super(handler, index, xPosition, yPosition);
        this.type = BaubleType.bySlotId(index);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        boolean valid = type.isItemValid(stack);
        IBauble impl = BaublesAPI.getBaubleImplementation(stack);
        if(impl != null)
            return valid && impl.canEquip(type, stack, ((BaublesInventoryCapability)getItemHandler()).getPlayer());
        return valid;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        if(getItem().isEmpty())
            return false;
        IBauble impl = BaublesAPI.getBaubleImplementation(getItem());
        if(impl != null)
            return impl.canUnequip(type, getItem(), playerIn);
        return true;
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        IBauble impl = BaublesAPI.getBaubleImplementation(pStack);
        if(impl != null) {
            if(impl.canUnequip(type, pStack, pPlayer)) {
                super.onTake(pPlayer, pStack);
                impl.onUnequipped(type, pStack, pPlayer);
            }
        } else {
            super.onTake(pPlayer, pStack);
        }
    }

    @Override
    public void set(ItemStack stack) {
        Player p = ((BaublesInventoryCapability)getItemHandler()).getPlayer();
        IBauble impl = BaublesAPI.getBaubleImplementation(getItem());
        if(!ItemStack.matches(getItem(), stack) && !getItem().isEmpty() && impl != null)
            impl.onUnequipped(type, getItem(), p);

        ItemStack old = getItem().copy();
        super.set(stack);
        IBauble implNew = BaublesAPI.getBaubleImplementation(stack);
        if(!ItemStack.matches(getItem(), old) && implNew != null)
            implNew.onEquipped(type, getItem(), p);
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, type.getPlaceholderTexture());
    }
}
