package tld.unknown.baubles.client.rendering;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tld.unknown.baubles.api.IBaubleRenderer;
import tld.unknown.baubles.api.IBaubleRenderers;

import java.util.Map;

public record BaubleRenderers(Map<ResourceLocation, IBaubleRenderer> renderers)  implements IBaubleRenderers {

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
}
