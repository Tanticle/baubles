package tld.unknown.baubles.networking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.baubles.api.BaublesData;

public record ClientboundSyncDataPacket(CompoundTag data) implements CustomPacketPayload {

    public ClientboundSyncDataPacket(final FriendlyByteBuf buf) {
        this(buf.readNbt());
    }

    @Override
    public ResourceLocation id() {
        return BaublesData.Networking.SYNC_DATA;
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeNbt(data);
    }
}
