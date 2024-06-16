package tld.unknown.baubles.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;

/**
 * A collection of events related to Baubles.
 * Unused for now, may implement it if there is demand.
 */
public class BaubleEvent extends Event implements IModBusEvent {

    private final BaubleType type;
    private final ItemStack item;
    private final Player player;

    public BaubleEvent(BaubleType type, ItemStack item, Player player) {
        this.type = type;
        this.item = item;
        this.player = player;
    }

    public BaubleType getType() { return type; }

    public ItemStack getItem() { return item; }

    public Player getPlayer() { return player; }

    public static class Equip extends BaubleEvent implements ICancellableEvent {
        public Equip(BaubleType type, ItemStack item, Player player) { super(type, item, player); }
    }

    public static class Unequip extends BaubleEvent implements ICancellableEvent {
        public Unequip(BaubleType type, ItemStack item, Player player) { super(type, item, player); }
    }

    public static class WornTick extends BaubleEvent {
        public WornTick(BaubleType type, ItemStack item, Player player) { super(type, item, player); }
    }
}
