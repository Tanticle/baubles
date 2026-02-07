package tld.unknown.baubles.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import tld.unknown.baubles.BaublesHolderAttachment;
import tld.unknown.baubles.BaublesMod;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.Baubles;
import tld.unknown.baubles.api.IBauble;

import java.util.List;
import java.util.Optional;

public class ExpandedInventoryMenu extends AbstractCraftingMenu {

    private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[] {
            InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS,
            InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
            InventoryMenu.EMPTY_ARMOR_SLOT_HELMET
    };
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[] {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 2, 2);
    private final ResultContainer resultSlots = new ResultContainer();
    private final Player player;

    private final int baublesInvIdStart;

    // Client Constructor
    public ExpandedInventoryMenu(int pConainterId, Inventory playerInventory) {
        this(pConainterId, playerInventory, Minecraft.getInstance().player.getData(BaublesMod.ATTACHMENT_BAUBLES));
    }

    // Server Constructor
    public ExpandedInventoryMenu(int pContainerId, Player p) {
        this(pContainerId, p.getInventory(), p.getData(BaublesMod.ATTACHMENT_BAUBLES));
    }

    private ExpandedInventoryMenu(int pContainerId, Inventory playerInventory, BaublesHolderAttachment baubles) {
        super(BaublesMod.MENU_EXPANDED_INVENTORY.get(), pContainerId, 2, 2);
        this.player = playerInventory.player;
        // Crafting Slots
        this.addSlot(new ResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 154, 28));
        for(int i = 0; i < 2; ++i)
            for(int ii = 0; ii < 2; ++ii)
                this.addSlot(new Slot(this.craftSlots, ii + i * 2, 116 + ii * 18, 18 + i * 18));

        // Armor Slots
        for(int i = 0; i < 4; ++i) {
            final EquipmentSlot equipmentslot = SLOT_IDS[i];
            this.addSlot(new Slot(playerInventory, 36 + (3 - i), 8, 8 + i * 18) {
                @Override
                public void setByPlayer(ItemStack p_270969_, ItemStack p_299918_) {
                    playerInventory.player.onEquipItem(equipmentslot, p_270969_, p_299918_);
                    super.setByPlayer(p_270969_, p_299918_);
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(ItemStack p_39746_) {
                    return p_39746_.canEquip(equipmentslot, playerInventory.player);
                }

                @Override
                public boolean mayPickup(Player p_39744_) {
                    ItemStack itemstack = this.getItem();
                    return (itemstack.isEmpty() || p_39744_.isCreative() || EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) && super.mayPickup(p_39744_);
                }

                @Override
                public ResourceLocation getNoItemIcon() {
                    return TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()];
                }
            });
        }

        // Main Inventory
        for(int i = 0; i < 3; ++i)
            for(int ii = 0; ii < 9; ++ii)
                this.addSlot(new Slot(playerInventory, ii + (i + 1) * 9, 8 + ii * 18, 84 + i * 18));

        // Hotbar
        for(int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));

        // Offhand
        this.addSlot(new Slot(playerInventory, 40, 96, 62) {
            @Override
            public void setByPlayer(ItemStack p_270479_, ItemStack p_299920_) {
                playerInventory.player.onEquipItem(EquipmentSlot.OFFHAND, p_270479_, p_299920_);
                super.setByPlayer(p_270479_, p_299920_);
            }

            @Override
            public ResourceLocation getNoItemIcon() {
                return InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD;
            }
        });

        // Baubles
        for(int i = 0; i < 2; i++)
            for(int ii = 0; ii < 4; ii++) {
                int slot = i * 4 + ii;
                if(slot > 6)
                    break;
                this.addSlot(new BaubleSlot(baubles, slot, i * 19 + 77, ii * 18 + 8));
            }

        this.baublesInvIdStart = slots.size() - BaublesHolderAttachment.INVENTORY_SIZE;
    }

    private boolean baubleMoveStack(ItemStack stack, int slotId) {
        Slot slot = getSlot(slotId);
        if(!slot.hasItem()) {
            if(!slot.mayPlace(stack))
                return true;
            slot.set(stack);
            return false;
        }
        ItemStack old = slot.getItem();
        if(old.isStackable() && ItemStack.isSameItemSameComponents(stack, old)) {
            int totalAmount = old.getCount() + stack.getCount();
            if(totalAmount <= old.getMaxStackSize()) {
                old.setCount(totalAmount);
                slot.setChanged();
                return false;
            } else {
                int difference = totalAmount - old.getMaxStackSize();
                old.setCount(old.getMaxStackSize());
                slot.setChanged();
                stack.shrink(difference);
                return true;
            }
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            EquipmentSlot equipmentslot = pPlayer.getEquipmentSlotForItem(itemstack);
            if(!(slot instanceof BaubleSlot)) {
                if(Baubles.API.isBaubleItem(itemstack1)) {
                    if(itemstack1.is(Baubles.Tags.ITEM_TRINKET)) {
                        for(int i = 0; i < BaublesHolderAttachment.INVENTORY_SIZE; i++) {
                            int slotId = baublesInvIdStart + i;
                            if(!baubleMoveStack(itemstack, slotId)) {
                                slot.set(ItemStack.EMPTY);
                                slot.setChanged();
                                return ItemStack.EMPTY;
                            }
                        }
                    } else {
                        for(BaubleType type : Baubles.API.getBaubleTypes(itemstack1)) {
                            int slotId = baublesInvIdStart + type.ordinal();
                            if(!baubleMoveStack(itemstack, slotId)) {
                                slot.set(ItemStack.EMPTY);
                                slot.setChanged();
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                    itemstack1 = itemstack.copy();
                }
            } else if(Baubles.API.hasBaubleImplementation(itemstack1)) {
                IBauble impl = Baubles.API.getBaubleImplementation(itemstack1);
                BaubleType type = BaubleType.bySlotId(pIndex - baublesInvIdStart);
                if (impl.canUnequip(type, itemstack1, player) && this.moveItemStackTo(itemstack1, 9, 45, false)) {
                    impl.onUnequipped(type, itemstack1, player);
                    return ItemStack.EMPTY;
                }
                itemstack1 = itemstack.copy();
            }
            if (pIndex == 0) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (pIndex >= 1 && pIndex < 5) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 5 && pIndex < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && !this.slots.get(8 - equipmentslot.getIndex()).hasItem()) {
                int i = 8 - equipmentslot.getIndex();
                if (!this.moveItemStackTo(itemstack1, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslot == EquipmentSlot.OFFHAND && !this.slots.get(45).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 9 && pIndex < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 36 && pIndex < 45) {
                if (!this.moveItemStackTo(itemstack1, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY, itemstack);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
            if (pIndex == 0) {
                pPlayer.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

	@Override
	public void slotsChanged(Container inventory) {
		if (this.player.level() instanceof ServerLevel level) {
			CraftingInput craftinginput = craftSlots.asCraftInput();
			ServerPlayer serverplayer = (ServerPlayer)player;
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<RecipeHolder<CraftingRecipe>> optional = level.getServer()
					.getRecipeManager()
					.getRecipeFor(RecipeType.CRAFTING, craftinginput, level, (RecipeHolder<CraftingRecipe>)null);
			if (optional.isPresent()) {
				RecipeHolder<CraftingRecipe> recipeholder = optional.get();
				CraftingRecipe craftingrecipe = recipeholder.value();
				if (resultSlots.setRecipeUsed(serverplayer, recipeholder)) {
					ItemStack itemstack1 = craftingrecipe.assemble(craftinginput, level.registryAccess());
					if (itemstack1.isItemEnabled(level.enabledFeatures())) {
						itemstack = itemstack1;
					}
				}
			}

			resultSlots.setItem(0, itemstack);
			setRemoteSlot(0, itemstack);
			serverplayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, itemstack));
		}
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void removed(Player player) {
		super.removed(player);
		this.resultSlots.clearContent();
		if (!player.level().isClientSide) {
			this.clearContainer(player, this.craftSlots);
		}
	}


	@Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    // Recipe Book

	@Override
	public Slot getResultSlot() {
		return this.slots.getFirst();
	}


	@Override
	protected Player owner() {
		return this.player;
	}

	@Override
	public List<Slot> getInputGridSlots() {
		return this.slots.subList(1, 5);
	}

	@Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }
}
