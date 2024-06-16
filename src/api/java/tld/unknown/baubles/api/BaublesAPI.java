package tld.unknown.baubles.api;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

public final class BaublesAPI {

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

    private static IBaubleRenderers renderers;

    public static IBaubleRenderers getRenderers() {
        return renderers;
    }

    @ApiStatus.Internal
    public static void setRenderHandlers(IBaubleRenderers renderers) {
        BaublesAPI.renderers = renderers;
    }
}
