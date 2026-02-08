package tld.unknown.baubles.client.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tld.unknown.baubles.api.IBaubleRenderer;

import java.util.HashMap;
import java.util.Map;

public class BaubleRenderers  {

    public final Map<ResourceLocation, IBaubleRenderer> renderers = new HashMap<>();

    public boolean renderDebugMode = false;

    public IBaubleRenderer getRenderer(ItemStack stack) {
        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return renderers.get(loc);
    }

    public void toggleRenderDebugMode() {
        renderDebugMode = !renderDebugMode;
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("msg.baubles.debug_" + (renderDebugMode ? "on" : "off")), true);
    }
}
