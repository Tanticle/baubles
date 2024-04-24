package tld.unknown.baubles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tld.unknown.baubles.api.*;
import tld.unknown.baubles.networking.ClientboundSyncDataPacket;
import tld.unknown.baubles.networking.NetworkHandler;
import tld.unknown.baubles.networking.ServerboundOpenInvPacket;

public final class EventHandlers {

    @EventBusSubscriber(modid = BaublesData.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static final class ModBusSubscriber {

        @SubscribeEvent
        public static void registerPackets(final RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar(BaublesData.MOD_ID);
            registrar.versioned(BaublesData.Networking.VERSION);
            registrar.commonToClient(ClientboundSyncDataPacket.TYPE, ClientboundSyncDataPacket.CODEC, NetworkHandler::clientHandleDataSync);
            registrar.playToServer(ServerboundOpenInvPacket.TYPE, ServerboundOpenInvPacket.CODEC, NetworkHandler::serverHandleOpenInv);
        }

        @SubscribeEvent
        public static void registerCapabilities(final RegisterCapabilitiesEvent event) {
            event.registerEntity(Capabilities.ItemHandler.ENTITY, EntityType.PLAYER, (player, ctx) -> player.getData(Registries.ATTACHMENT_BAUBLES));
        }
    }

    @EventBusSubscriber(modid = BaublesData.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static final class ForgeBusSubscriber {

        @SubscribeEvent
        public static void playerTick(final TickEvent.PlayerTickEvent event) {
            if(event.phase == TickEvent.Phase.END) {
                IBaubleHolder baubles = event.player.getData(Registries.ATTACHMENT_BAUBLES);
                for (int i = 0; i < IBaubleHolder.INVENTORY_SIZE; i++) {
                    ItemStack slot = baubles.getAllSlots()[i];
                    IBauble impl = BaublesAPI.getBaubleImplementation(slot);
                    if(slot != ItemStack.EMPTY && impl != null) {
                        impl.onWornTick(BaubleType.bySlotId(i), slot, event.player);
                    }
                }
            }
        }
    }
}
