package tld.unknown.baubles.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import tld.unknown.baubles.api.BaublesData;

import java.util.List;

public record ClientboundSyncDataPacket(List<ItemStack> data) implements CustomPacketPayload {

    public static final Type<ClientboundSyncDataPacket> TYPE = new Type<>(BaublesData.Networking.SYNC_DATA);

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncDataPacket> STREAM_CODEC = ItemStack.OPTIONAL_LIST_STREAM_CODEC.map(ClientboundSyncDataPacket::new, ClientboundSyncDataPacket::data);

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
