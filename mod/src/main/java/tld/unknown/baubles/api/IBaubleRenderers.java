package tld.unknown.baubles.api;

import net.minecraft.resources.ResourceLocation;

public interface IBaubleRenderers {
    void registerRenderer(ResourceLocation key, IBaubleRenderer renderer);
    void registerRenderer(ResourceLocation key, IBaubleRenderer renderer, boolean replace);
}