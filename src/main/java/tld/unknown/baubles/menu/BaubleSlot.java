package tld.unknown.baubles.menu;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;
import tld.unknown.baubles.BaublesHolderAttachment;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.Baubles;
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
        IBauble impl = Baubles.API.getBaubleImplementation(stack);
        if(impl != null)
            return valid && impl.canEquip(type, stack, ((BaublesHolderAttachment)getItemHandler()).getPlayer());
        return valid;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        if(getItem().isEmpty())
            return false;
        IBauble impl = Baubles.API.getBaubleImplementation(getItem());
        if(impl != null)
            return impl.canUnequip(type, getItem(), playerIn);
        return true;
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        IBauble impl = Baubles.API.getBaubleImplementation(pStack);
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
        IBauble impl = Baubles.API.getBaubleImplementation(currentStack);
        if(impl != null && !isSame)
            impl.onUnequipped(type, getItem(), p);
        super.set(stack);
        currentStack = getItem();
        IBauble implNew = Baubles.API.getBaubleImplementation(currentStack);
        if(implNew != null && !isSame)
            implNew.onEquipped(type, currentStack, p);
    }

	@Override
	public @Nullable ResourceLocation getNoItemIcon() {
		return type.getPlaceholderTexture();
	}
}
