package tld.unknown.baubles.client.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLLoader;
import tld.unknown.baubles.api.IBaubleRenderer;
import tld.unknown.baubles.api.IBaubleRenderers;

import java.util.Map;

public class BaubleRenderers implements IBaubleRenderers {

    private final Map<ResourceLocation, IBaubleRenderer> renderers;

    public boolean renderDebugMode = false;

    public BaubleRenderers(Map<ResourceLocation, IBaubleRenderer> renderers) {
        this.renderers = renderers;
    }

    public IBaubleRenderer getRenderer(ItemStack stack) {
        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return renderers.get(loc);
    }

    @Override
    public void registerRenderer(ResourceLocation key, IBaubleRenderer renderer, boolean replace) {
        if(!replace)
            renderers.putIfAbsent(key, renderer);
        else
            renderers.put(key, renderer);
    }

    @Override
    public void registerRenderer(ResourceLocation key, IBaubleRenderer renderer) {
        registerRenderer(key, renderer, false);
    }

    public void toggleRenderDebugMode() {
        if(FMLLoader.isProduction())
            return;
        renderDebugMode = !renderDebugMode;
        Minecraft.getInstance().player.sendSystemMessage(Component.translatable("msg.baubles.debug_" + (renderDebugMode ? "on" : "off")));
    }
}
