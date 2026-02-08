package tld.unknown.baubles.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;
import tld.unknown.baubles.api.BaublesEvent;
import tld.unknown.baubles.api.Baubles;
import tld.unknown.baubles.client.rendering.BaubleRenderers;

@Mod(value = Baubles.MOD_ID, dist = Dist.CLIENT)
public final class BaublesClient {

    public static final BaubleRenderers RENDERERS = new BaubleRenderers();

    public static final KeyMapping KEY_INVENTORY = new KeyMapping("key.baubles.inventory",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            "key.categories.inventory");

    public static final KeyMapping KEY_DEBUG = new KeyMapping("key.baubles.toggle_debug",
            KeyConflictContext.UNIVERSAL, KeyModifier.SHIFT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            "key.categories.misc");

	@EventBusSubscriber(modid = Baubles.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static final class ModBusSubscriber {

		@SubscribeEvent
		public static void onClientInit(final FMLClientSetupEvent event) {
			event.enqueueWork(() -> ModLoader.postEvent(new BaublesEvent.RendererRegistration()));
		}
	}
}
