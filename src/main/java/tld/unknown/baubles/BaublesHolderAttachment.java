package tld.unknown.baubles;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.IBaublesHolder;
import tld.unknown.baubles.networking.ClientboundSyncDataPacket;

import java.util.List;

public class BaublesHolderAttachment extends ItemStackHandler implements IBaublesHolder {

    public static final int INVENTORY_SIZE = 7;

    private final Player player;

    public BaublesHolderAttachment(Player p) {
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
        return BaubleType.bySlotId(slot).isItemValid(stack) || stack == ItemStack.EMPTY;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if(!player.level().isClientSide()) {
            ((ServerPlayer)player).connection.send(new ClientboundSyncDataPacket(player.getData(Registries.ATTACHMENT_BAUBLES).stacks));
        }
    }

    public void updateSlots(List<ItemStack> stacks) {
        for(int i = 0; i < INVENTORY_SIZE; i++) {
            ItemStack stack = stacks.get(i);
            setStackInSlot(i, stack != null ? stack : ItemStack.EMPTY);
        }
    }

    // IBaubleCapability

    @Override
    public ItemStack getBaubleInSlot(@NotNull BaubleType type) {
        return getStackInSlot(type.ordinal());
    }

    @Override
    public boolean setBaubleInSlot(@NotNull BaubleType type, @NotNull ItemStack stack) {
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
