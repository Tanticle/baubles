package tld.unknown.baubles.networking;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.Nullable;
import tld.unknown.baubles.menu.ExpandedInventoryMenu;

public final class NetworkHandler {
    public static void serverHandleOpenInv(ServerboundOpenBaublesInvPacket packet, IPayloadContext ctx) {
        ServerPlayer player = (ServerPlayer)ctx.player();
        player.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable("container.crafting");
            }

            @Override
            public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new ExpandedInventoryMenu(i, player);
            }

            @Override
            public boolean shouldTriggerClientSideContainerClosingOnOpen() {
                return false;
            }
        });
    }
}
