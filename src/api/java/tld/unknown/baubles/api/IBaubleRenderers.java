package tld.unknown.baubles.api;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * A simple interface to hold and register an {@link IBaubleRenderer} to.
 * @author Tom Tanticle
 */
public interface IBaubleRenderers {

    /**
     * Registers a new {@link IBaubleRenderer} for the given item {@link ResourceLocation}. Will not replace existing registrations.
     * @param key The {@link ResourceLocation} of the item referencing this {@link IBaubleRenderer}.
     * @param renderer The instance of the {@link IBaubleRenderer} to be registered.
     */
    void registerRenderer(@NotNull ResourceLocation key, @NotNull IBaubleRenderer renderer);

    /**
     * Registers a new {@link IBaubleRenderer} for the given item {@link ResourceLocation}.
     * @param key The {@link ResourceLocation} of the item referencing this {@link IBaubleRenderer}.
     * @param renderer The instance of the {@link IBaubleRenderer} to be registered.
     * @param replace Whether to replace an already existing registration for this item {@link ResourceLocation}.
     */
    void registerRenderer(@NotNull ResourceLocation key, @NotNull IBaubleRenderer renderer, boolean replace);
}