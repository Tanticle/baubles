package tld.unknown.baubles.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import tld.unknown.baubles.api.BaublesData;

public record ServerboundOpenBaublesInvPacket(float mouseX, float mouseY) implements CustomPacketPayload {

    public static final Type<ServerboundOpenBaublesInvPacket> TYPE = new Type<>(BaublesData.Networking.OPEN_INV);

    public static final StreamCodec<FriendlyByteBuf, ServerboundOpenBaublesInvPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ServerboundOpenBaublesInvPacket::mouseX,
            ByteBufCodecs.FLOAT, ServerboundOpenBaublesInvPacket::mouseY,
            ServerboundOpenBaublesInvPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
