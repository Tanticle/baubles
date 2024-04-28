package tld.unknown.baubles.menu;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;
import tld.unknown.baubles.api.BaublesAPI;
import tld.unknown.baubles.BaublesHolderAttachment;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.IBauble;

public class BaubleSlot extends SlotItemHandler {

    private final BaubleType type;

    public BaubleSlot(BaublesHolderAttachment handler, int index, int xPosition, int yPosition) {
        super(handler, index, xPosition, yPosition);
        this.type = BaubleType.bySlotId(index);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        boolean valid = type.isItemValid(stack);
        IBauble impl = BaublesAPI.getBaubleImplementation(stack);
        if(impl != null)
            return valid && impl.canEquip(type, stack, ((BaublesHolderAttachment)getItemHandler()).getPlayer());
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
                setChanged();
                impl.onUnequipped(type, pStack, pPlayer);
            }
        } else {
            setChanged();
        }
    }

    @Override
    public void set(ItemStack stack) {
        ItemStack currentStack = getItem();
        boolean isSame = ItemStack.isSameItemSameComponents(currentStack, stack);
        Player p = ((BaublesHolderAttachment)getItemHandler()).getPlayer();
        IBauble impl = BaublesAPI.getBaubleImplementation(currentStack);
        if(impl != null && !isSame)
            impl.onUnequipped(type, getItem(), p);
        super.set(stack);
        currentStack = getItem();
        IBauble implNew = BaublesAPI.getBaubleImplementation(currentStack);
        if(implNew != null && !isSame)
            implNew.onEquipped(type, currentStack, p);
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, type.getPlaceholderTexture());
    }
}
