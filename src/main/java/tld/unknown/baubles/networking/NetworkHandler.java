package tld.unknown.baubles.networking;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tld.unknown.baubles.menu.ExpandedInventoryMenu;

public final class NetworkHandler {
    public static void serverHandleOpenInv(ServerboundOpenBaublesInvPacket packet, IPayloadContext ctx) {
        ServerPlayer player = (ServerPlayer)ctx.player();
        player.openMenu(new SimpleMenuProvider((id, inv, p) -> new ExpandedInventoryMenu(id, p), Component.translatable("container.crafting")));
    }
}
