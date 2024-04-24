package tld.unknown.baubles;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.IBaubleHolder;
import tld.unknown.baubles.networking.ClientboundSyncDataPacket;

public class BaublesInventoryCapability extends ItemStackHandler implements IBaubleHolder {

    public static final int INVENTORY_SIZE = 7;

    private final Player player;

    public BaublesInventoryCapability(Player p) {
        super(INVENTORY_SIZE);
        this.player = p;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if(!isItemValid(slot, stack))
            return;
        super.setStackInSlot(slot, stack);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return BaubleType.bySlotId(slot).isItemValid(stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if(!player.level().isClientSide()) {
            ((ServerPlayer)player).connection.send(new ClientboundSyncDataPacket(this.serializeNBT(this.getPlayer().registryAccess())));
        }
    }

    // IBaubleCapability

    @Override
    public ItemStack getBaubleInSlot(BaubleType type) {
        return getStackInSlot(type.ordinal());
    }

    @Override
    public boolean setBaubleInSlot(BaubleType type, ItemStack stack) {
        int slot = type.ordinal();
        if(!isItemValid(slot, stack))
            return false;
        setStackInSlot(slot, stack);
        return true;
    }

    @Override
    public ItemStack[] getAllSlots() {
        return stacks.toArray(new ItemStack[0]);
    }


}
