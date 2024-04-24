package tld.unknown.baubles.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import tld.unknown.baubles.api.BaublesData;

public record ClientboundSyncDataPacket(CompoundTag data) implements CustomPacketPayload {

    public static final Type<ClientboundSyncDataPacket> TYPE = new Type<>(BaublesData.Networking.SYNC_DATA);

    public static final StreamCodec<ByteBuf, ClientboundSyncDataPacket> CODEC = ByteBufCodecs.COMPOUND_TAG.map(ClientboundSyncDataPacket::new, ClientboundSyncDataPacket::data);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
