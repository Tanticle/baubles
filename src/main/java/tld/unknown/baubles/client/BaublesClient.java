package tld.unknown.baubles.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class BaublesClient {

    public static final KeyMapping KEY_INVENTORY = new KeyMapping("key.baubles.inventory",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B,
            "key.categories.inventory");
}
