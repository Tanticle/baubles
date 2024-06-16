package tld.unknown.baubles.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import tld.unknown.baubles.client.rendering.BaubleRenderers;

import java.util.HashMap;

public final class BaublesClient {

    public static final BaubleRenderers RENDERERS = new BaubleRenderers(new HashMap<>());

    public static final KeyMapping KEY_INVENTORY = new KeyMapping("key.baubles.inventory",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            "key.categories.inventory");
}
