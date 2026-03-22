package tld.unknown.baubles.api;

import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A dataclass used to pass information to the rendering pass of the baubles {@link net.minecraft.client.renderer.entity.layers.RenderLayer}.
 * If additional information is needed, this class needs to be implemented by one of your own making, and then constructed,
 * populated and returned in {@link IBaubleRenderer#prepareRenderState(ItemStack, BaubleType, Avatar, Level)}. Alternatively,
 * if no additional data is needed, {@link BaubleRenderContext#create(ItemStack, BaubleType)} may be used for a default implementation.
 * @author Tom Tanticle
 */
public abstract class BaubleRenderContext {

	private final ItemStack baubleStack;
	private final BaubleType baubleType;

	public BaubleRenderContext(ItemStack baubleStack, BaubleType baubleType) {
		this.baubleStack = baubleStack;
		this.baubleType = baubleType;
	}

	/**
	 * Convenience method to create a default implementation of {@link BaubleRenderContext} only containing the necessary
	 * {@link BaubleType} slot and the given bauble {@link ItemStack}. Only use this when no additional data is necessary
	 * to render your bauble.
	 * @param baubleStack The {@link ItemStack} of the bauble currently being renderer.
	 * @param baubleType The {@link BaubleType} slot currently being renderer.
	 * @return A default implemented of {@link BaubleRenderContext} only containing the bare necessities.
	 */
	public static BaubleRenderContext create(ItemStack baubleStack, BaubleType baubleType) {
		return new BaubleRenderContext(baubleStack, baubleType) { };
	}

	public BaubleType baubleType() {
		return baubleType;
	}

	public ItemStack baubleStack() {
		return baubleStack;
	}

}
