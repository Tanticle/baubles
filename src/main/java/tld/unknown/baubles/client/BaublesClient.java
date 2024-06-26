package tld.unknown.baubles.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;
import tld.unknown.baubles.api.BaublesAPI;
import tld.unknown.baubles.api.BaublesData;
import tld.unknown.baubles.client.rendering.BaubleRenderers;

import java.util.HashMap;

@Mod(value = BaublesData.MOD_ID, dist = Dist.CLIENT)
public final class BaublesClient {

    public static final BaubleRenderers RENDERERS = new BaubleRenderers(new HashMap<>());

    public static final KeyMapping KEY_INVENTORY = new KeyMapping("key.baubles.inventory",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            "key.categories.inventory");

    public static final KeyMapping KEY_DEBUG = new KeyMapping("key.baubles.toggle_debug",
            KeyConflictContext.UNIVERSAL, KeyModifier.SHIFT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            "key.categories.misc");

    public BaublesClient() {
        BaublesAPI.setRenderHandlers(RENDERERS);
    }
}
