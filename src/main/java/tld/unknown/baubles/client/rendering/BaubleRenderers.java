package tld.unknown.baubles.client.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import tld.unknown.baubles.api.BaubleRenderContext;
import tld.unknown.baubles.api.IBaubleRenderer;

import java.util.HashMap;
import java.util.Map;

public class BaubleRenderers  {

    public final Map<Identifier, IBaubleRenderer<? extends BaubleRenderContext>> renderers = new HashMap<>();

    public boolean renderDebugMode = false;

    public IBaubleRenderer<? extends BaubleRenderContext> getRenderer(Identifier id) {
        return renderers.get(id);
    }

    public void toggleRenderDebugMode() {
        renderDebugMode = !renderDebugMode;
        Minecraft.getInstance().player.sendOverlayMessage(Component.translatable("msg.baubles.debug_" + (renderDebugMode ? "on" : "off")));
    }
}
