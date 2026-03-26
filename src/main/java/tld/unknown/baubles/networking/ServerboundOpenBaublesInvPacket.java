package tld.unknown.baubles.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import tld.unknown.baubles.api.Baubles;

public record ServerboundOpenBaublesInvPacket() implements CustomPacketPayload {

    public static final Type<ServerboundOpenBaublesInvPacket> TYPE = new Type<>(Baubles.Networking.OPEN_INV);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
