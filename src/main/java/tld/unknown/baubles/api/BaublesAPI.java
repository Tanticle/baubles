package tld.unknown.baubles.api;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public final class BaublesAPI {

    private static final Map<ResourceLocation, IBaubleRenderer> renderers = new HashMap<>();

    public static boolean hasBaubleImplementation(ItemStack stack) {
        return getBaubleImplementation(stack) != null;
    }

    public static IBauble getBaubleImplementation(ItemStack stack) {
        return stack.getItem() instanceof IBauble b ? b : stack.getCapability(BaublesData.CapabilitiesAttachments.CAPABILITY_BAUBLE);
    }

    public static boolean isBaubleItem(ItemStack stack) {
        return hasBaubleImplementation(stack) || BaubleType.hasBaubleTags(stack);
    }

    public static List<BaubleType> getBaubleTypes(ItemStack stack) {
        List<BaubleType> set = new ArrayList<>();
        if(!isBaubleItem(stack))
            return set;
        for(BaubleType baubleType : BaubleType.values())
            if(baubleType.isItemValid(stack))
                set.add(baubleType);
        return set;
    }

    public static void registerRenderer(ResourceLocation key, IBaubleRenderer renderer) {
        registerRenderer(key, renderer, false);
    }

    public static void registerRenderer(ResourceLocation key, IBaubleRenderer renderer, boolean replace) {
        if(!replace)
            renderers.putIfAbsent(key, renderer);
        else
            renderers.put(key, renderer);
    }

    public static IBaubleRenderer getRenderer(ItemStack stack) {
        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return renderers.get(loc);
    }
}
