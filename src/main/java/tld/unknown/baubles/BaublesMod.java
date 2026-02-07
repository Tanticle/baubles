package tld.unknown.baubles;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tld.unknown.baubles.api.*;
import tld.unknown.baubles.client.BaublesClient;
import tld.unknown.baubles.menu.ExpandedInventoryMenu;

import java.util.ArrayList;
import java.util.List;

@Mod(Baubles.MOD_ID)
public class BaublesMod implements IBaublesAPI {

	private static final DeferredRegister<AttachmentType<?>> REGISTRY_ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Baubles.MOD_ID);
	private static final DeferredRegister<MenuType<?>> REGISTRY_MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Baubles.MOD_ID);
	private static final DeferredRegister<DataComponentType<?>> REGISTRY_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Baubles.MOD_ID);

	/* -----------------------------------------------------------------------------------------------------------*/

	public static final DeferredHolder<AttachmentType<?>, AttachmentType<BaublesHolderAttachment>> ATTACHMENT_BAUBLES = REGISTRY_ATTACHMENTS.register(
			"baubles",
			() -> AttachmentType.serializable((holder) -> new BaublesHolderAttachment((Player)holder)).copyOnDeath().build());

	public static final DeferredHolder<MenuType<?>, MenuType<ExpandedInventoryMenu>> MENU_EXPANDED_INVENTORY = REGISTRY_MENU_TYPES.register(
			"baubles_inventory",
			() -> new MenuType<>(ExpandedInventoryMenu::new, FeatureFlags.DEFAULT_FLAGS));

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<BaubleType>>> COMPONENT_BAUBLE = REGISTRY_COMPONENTS.register(
			"bauble",
			() -> Baubles.COMPONENT_BAUBLE);

	/* -----------------------------------------------------------------------------------------------------------*/

    public BaublesMod(IEventBus bus) {
		REGISTRY_ATTACHMENTS.register(bus);
		REGISTRY_MENU_TYPES.register(bus);
		REGISTRY_COMPONENTS.register(bus);

		Baubles.API = this;
    }

	// Baubles API Implementation

	public boolean hasBaubleImplementation(@NotNull ItemStack stack) {
		return getBaubleImplementation(stack) != null;
	}

	public @Nullable IBauble getBaubleImplementation(@NotNull ItemStack stack) {
		return stack.getItem() instanceof IBauble b ? b : stack.getCapability(Baubles.CAPABILITY_BAUBLE);
	}

	public boolean isBaubleItem(@NotNull ItemStack stack) {
		return hasBaubleImplementation(stack) || BaubleType.hasBaubleData(stack);
	}

	public List<BaubleType> getBaubleTypes(@NotNull ItemStack stack) {
		List<BaubleType> set = new ArrayList<>();
		if(!isBaubleItem(stack))
			return set;
		if(stack.has(COMPONENT_BAUBLE))
			return stack.get(COMPONENT_BAUBLE);
		for(BaubleType baubleType : BaubleType.values())
			if(baubleType.isItemValid(stack))
				set.add(baubleType);
		return set;
	}

	public IBaubleRenderers getRenderers() {
		return BaublesClient.RENDERERS;
	}
}
