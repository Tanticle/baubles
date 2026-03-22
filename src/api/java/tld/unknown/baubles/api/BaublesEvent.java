package tld.unknown.baubles.api;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.NotNull;

/**
 * A collection of events related to the Baubles mod.
 * @author Tom Tanticle
 */
public final class BaublesEvent {

	private static abstract class EquipmentEvent extends Event {

		private final BaubleType type;
		private final ItemStack item;
		private final Player player;

		private EquipmentEvent(BaubleType type, ItemStack item, Player player) {
			this.type = type;
			this.item = item;
			this.player = player;
		}

		/**
		 * Returns the current {@link BaubleType} slot for the current {@link ItemStack}.
		 * @return The current {@link BaubleType}.
		 */
		public BaubleType getType() { return type; }

		/**
		 * Returns the {@link ItemStack} in the current {@link BaubleType} slot..
		 * @return The current {@link ItemStack}.
		 */
		public ItemStack getItem() { return item; }

		/**
		 * Returns the {@link ItemStack} in the current {@link BaubleType} slot..
		 * @return The current {@link ItemStack}.
		 */
		public Player getPlayer() { return player; }
	}

	/**
	 * Event fired whenever a {@link ItemStack} is attempted to be put into a bauble slot. If cancelled,
	 * the action is aborted and the {@link ItemStack} will not be deposited into the slot. If an item is already in
	 * the slot, an {@link BaublesEvent.Unequip} is fired as well.
	 * <p>
	 * Will only fire on the server, and before the {@link IBauble#onEquipped(BaubleType, ItemStack, Player)} function of an {@link IBauble} item.
	 */
	public static class Equip extends EquipmentEvent implements ICancellableEvent {
		public Equip(BaubleType type, ItemStack item, Player player) { super(type, item, player); }
	}

	/**
	 * Event fired whenever a {@link ItemStack} is attempted to be removed from a bauble slot. If canceled,
	 * the action is aborted and the {@link ItemStack} remains in the slot.
	 * <p>
	 * Will only fire on the server, and before the {@link IBauble#onUnequipped(BaubleType, ItemStack, Player)} function of an {@link IBauble} item.
	 */
	public static class Unequip extends EquipmentEvent implements ICancellableEvent {
		public Unequip(BaubleType type, ItemStack item, Player player) { super(type, item, player); }
	}

	/**
	 * Event fired every tick for each {@link ItemStack} currently equipped in a bauble slot.
	 * <p>
	 * Will fire on the client and server, and before the {@link IBauble#onWornTick(BaubleType, ItemStack, Player)} function of an {@link IBauble} item.
	 */
	public static class WornTick extends EquipmentEvent {
		public WornTick(BaubleType type, ItemStack item, Player player) { super(type, item, player); }
	}

	/**
	 * The registration event used to add {@link IBaubleRenderer} for bauble items to the game. Fires at the same time as {@link net.neoforged.fml.event.lifecycle.FMLClientSetupEvent}.
	 * <p>
	 * Will only fire on the client.
	 */
	public static class RendererRegistration extends Event implements IModBusEvent {

		/**
		 * Registers a new {@link IBaubleRenderer} for the given item {@link Identifier}. Will not replace existing registrations.
		 * @param key The {@link Identifier} of the item referencing this {@link IBaubleRenderer}.
		 * @param renderer The instance of the {@link IBaubleRenderer} to be registered.
		 */
		public synchronized <CTX extends BaubleRenderContext> void registerRenderer(@NotNull Identifier key, @NotNull IBaubleRenderer<CTX> renderer) {
			registerRenderer(key, renderer, false);
		}

		/**
		 * Registers a new {@link IBaubleRenderer} for the given item {@link Identifier}.
		 * @param key The {@link Identifier} of the item referencing this {@link IBaubleRenderer}.
		 * @param renderer The instance of the {@link IBaubleRenderer} to be registered.
		 * @param replace Whether to replace an already existing registration for this item {@link Identifier}.
		 */
		public synchronized <CTX extends BaubleRenderContext> void registerRenderer(@NotNull Identifier key, @NotNull IBaubleRenderer<CTX> renderer, boolean replace) {
			if(!replace)
				Baubles.API.getBaubleRenderers().putIfAbsent(key, renderer);
			else
				Baubles.API.getBaubleRenderers().put(key, renderer);
		}
	}
}
