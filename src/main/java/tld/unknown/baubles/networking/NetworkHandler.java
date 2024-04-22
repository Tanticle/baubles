package tld.unknown.baubles.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import tld.unknown.baubles.BaublesInventoryCapability;
import tld.unknown.baubles.Registries;
import tld.unknown.baubles.menu.ExpandedInventoryMenu;

public final class NetworkHandler {

    public static void clientHandleDataSync(ClientboundSyncDataPacket packet, IPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            BaublesInventoryCapability cap = new BaublesInventoryCapability(Minecraft.getInstance().player);
            cap.deserializeNBT(packet.data());
            Minecraft.getInstance().player.setData(Registries.ATTACHMENT_BAUBLES, cap);
        });
    }

    public static void serverHandleOpenInv(ServerboundOpenInvPacket packet, PlayPayloadContext ctx) {
        ServerPlayer player = (ServerPlayer)ctx.player().get();
        if(packet.isCustomInv())
            player.openMenu(new SimpleMenuProvider((id, inv, p) -> new ExpandedInventoryMenu(id, p), Component.translatable("container.crafting")));
    }
}
