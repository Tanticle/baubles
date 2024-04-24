package tld.unknown.baubles.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import tld.unknown.baubles.api.BaublesData;

public record ServerboundOpenInvPacket(boolean isCustomInv, float mouseX, float mouseY) implements CustomPacketPayload {

    public static final Type<ServerboundOpenInvPacket> TYPE = new Type<>(BaublesData.Networking.OPEN_INV);

    public static final StreamCodec<FriendlyByteBuf, ServerboundOpenInvPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ServerboundOpenInvPacket::isCustomInv,
            ByteBufCodecs.FLOAT, ServerboundOpenInvPacket::mouseX,
            ByteBufCodecs.FLOAT, ServerboundOpenInvPacket::mouseY,
            ServerboundOpenInvPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
