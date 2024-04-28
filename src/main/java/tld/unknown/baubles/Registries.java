package tld.unknown.baubles;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import tld.unknown.baubles.api.BaublesData;
import tld.unknown.baubles.menu.ExpandedInventoryMenu;

public final class Registries {

    private static final DeferredRegister<AttachmentType<?>> REGISTRY_ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, BaublesData.MOD_ID);
    private static final DeferredRegister<MenuType<?>> REGISTRY_MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, BaublesData.MOD_ID);

    /* -----------------------------------------------------------------------------------------------------------*/

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BaublesHolderAttachment>> ATTACHMENT_BAUBLES = REGISTRY_ATTACHMENTS.register(
            BaublesData.CapabilitiesAttachments.ID_ATTACHMENT_BAUBLES.getPath(),
            () -> AttachmentType.serializable((holder) -> new BaublesHolderAttachment((Player)holder)).copyOnDeath().build());

    public static final DeferredHolder<MenuType<?>, MenuType<ExpandedInventoryMenu>> MENU_EXPANDED_INVENTORY = REGISTRY_MENU_TYPES.register(
            "baubles_inventory",
            () -> new MenuType<>(ExpandedInventoryMenu::new, FeatureFlags.DEFAULT_FLAGS));

    /* -----------------------------------------------------------------------------------------------------------*/

    public static void init(IEventBus bus) {
        REGISTRY_ATTACHMENTS.register(bus);
        REGISTRY_MENU_TYPES.register(bus);
    }
}
