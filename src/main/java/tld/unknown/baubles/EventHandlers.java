package tld.unknown.baubles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tld.unknown.baubles.api.*;
import tld.unknown.baubles.networking.ClientboundSyncDataPacket;
import tld.unknown.baubles.networking.NetworkHandler;
import tld.unknown.baubles.networking.ServerboundOpenBaublesInvPacket;

public final class EventHandlers {

    @EventBusSubscriber(modid = BaublesData.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static final class ModBusSubscriber {

        @SubscribeEvent
        public static void registerPackets(final RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar(BaublesData.MOD_ID);
            registrar.versioned(BaublesData.Networking.VERSION);
            registrar.commonToClient(ClientboundSyncDataPacket.TYPE, ClientboundSyncDataPacket.CODEC, NetworkHandler::clientHandleDataSync);
            registrar.playToServer(ServerboundOpenBaublesInvPacket.TYPE, ServerboundOpenBaublesInvPacket.CODEC, NetworkHandler::serverHandleOpenInv);
        }

        @SubscribeEvent
        public static void registerCapabilities(final RegisterCapabilitiesEvent event) {
            event.registerEntity(Capabilities.ItemHandler.ENTITY, EntityType.PLAYER, (player, ctx) -> player.getData(Registries.ATTACHMENT_BAUBLES));
        }
    }

    @EventBusSubscriber(modid = BaublesData.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static final class ForgeBusSubscriber {

        @SubscribeEvent
        public static void playerTick(final PlayerTickEvent.Post event) {
            IBaublesHolder baubles = event.getEntity().getData(Registries.ATTACHMENT_BAUBLES);
            for (int i = 0; i < IBaublesHolder.INVENTORY_SIZE; i++) {
                ItemStack slot = baubles.getAllSlots()[i];
                IBauble impl = BaublesAPI.getBaubleImplementation(slot);
                if(slot != ItemStack.EMPTY && impl != null) {
                    impl.onWornTick(BaubleType.bySlotId(i), slot, event.getEntity());
                }
            }
        }


        @SubscribeEvent
        public static void datapackSync(final OnDatapackSyncEvent event) {
            event.getRelevantPlayers().forEach(player -> {
                BaublesHolderAttachment holder = player.getData(Registries.ATTACHMENT_BAUBLES);
                for (BaubleType value : BaubleType.values()) {
                    ItemStack itemCopy = holder.getBaubleInSlot(value).copy();
                    if(!value.isItemValid(itemCopy)) {
                        holder.setBaubleInSlot(value, ItemStack.EMPTY);
                        player.addItem(itemCopy);
                    }
                }
                player.connection.send(new ClientboundSyncDataPacket(holder.serializeNBT(player.level().registryAccess())));
            });
        }
    }
}
