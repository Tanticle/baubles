package tld.unknown.baubles;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jetbrains.annotations.NotNull;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.IBaublesHolder;

import java.util.Arrays;
import java.util.List;

public class BaublesHolderAttachment extends ItemStacksResourceHandler implements IBaublesHolder {

    public static final int INVENTORY_SIZE = 7;

	private final Avatar avatar;

    public BaublesHolderAttachment(Avatar avatar) {
        super(INVENTORY_SIZE);
		this.avatar = avatar;
    }

	public BaublesHolderAttachment(List<ItemStack> items) {
		super(INVENTORY_SIZE);
		updateSlots(items);
		this.avatar = null;
	}

	@Override
	public void set(int index, ItemResource resource, int amount) {
		if(!isValid(index, resource))
			return;
		super.set(index, resource, resource == ItemResource.EMPTY ? 0 : amount);
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		return BaubleType.bySlotId(index).isItemValid(resource.toStack()) || resource.isEmpty();
	}

	public void updateSlots(List<ItemStack> stacks) {
        for(int i = 0; i < INVENTORY_SIZE; i++) {
            ItemStack stack = stacks.get(i);
            set(i, stack != null ? ItemResource.of(stack) : ItemResource.EMPTY, stacks.size());
        }
    }

	@Override
	protected void onContentsChanged(int index, ItemStack previousContents) {
		if(avatar != null)
			avatar.syncData(BaublesMod.ATTACHMENT_BAUBLES);
	}

	public Player getPlayer() {
		return this.avatar == null ? Minecraft.getInstance().player : (Player)Minecraft.getInstance().player;
	}

	// IBaubleCapability

    @Override
    public ItemStack getBaubleInSlot(@NotNull BaubleType type) {
        return getResource(type.ordinal()).toStack();
    }

    @Override
    public boolean setBaubleInSlot(@NotNull BaubleType type, @NotNull ItemStack stack) {
        int slot = type.ordinal();
        if(!isValid(slot, ItemResource.of(stack)))
            return false;
        set(slot, ItemResource.of(stack), stack.getCount());
        return true;
    }

    @Override
    public ItemStack[] getAllSlots() {
        return stacks.toArray(new ItemStack[0]);
    }

	public static final StreamCodec<RegistryFriendlyByteBuf, BaublesHolderAttachment> STREAM_CODEC = ItemStack.OPTIONAL_LIST_STREAM_CODEC.map(
			BaublesHolderAttachment::new,
			attachment -> Arrays.stream(attachment.getAllSlots()).toList());
}
