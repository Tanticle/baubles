package tld.unknown.baubles.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import tld.unknown.baubles.api.BaublesData;

public record ServerboundOpenInvPacket(boolean isCustomInv, float mouseX, float mouseY) implements CustomPacketPayload {

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeBoolean(isCustomInv);
        pBuffer.writeFloat(mouseX);
        pBuffer.writeFloat(mouseY);
    }

    @Override
    public ResourceLocation id() {
        return BaublesData.Networking.OPEN_INV;
    }
}
