package tld.unknown.baubles.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

/**
 * The renderer interface for an {@link IBauble} implementation. Must be registered in the given {@link IBaubleRenderers}.
 * @see <a href=https://github.com/Tanticle/baubles/wiki/Rendering-Your-Bauble>Baubles 2 Wiki on Rendering</a>
 * @author Tom Tanticle
 */
public interface IBaubleRenderer {

    /**
     * Rendering method for the head model part.
     * @param pose The given {@link PoseStack}. Comes pre-transformed with the underlying {@link net.minecraft.client.model.PlayerModel}.
     * @param bufferSource The given {@link MultiBufferSource} of the rendering pass.
     * @param packetLight The current light information packed as an {@link Integer}.
     * @param deltaTick Current frame delta since the last rendered frame.
     * @param p The current local {@link Player}.
     * @param stack The {@link ItemStack} causing this renderer to be called.
     * @param slot The {@link BaubleType} of the {@link IBaublesHolder} slot the given {@link ItemStack} is in.
     */
    default void renderBody(PoseStack pose,
                            MultiBufferSource bufferSource,
                            int packetLight,
                            float deltaTick,
                            Player p,
                            ItemStack stack,
                            BaubleType slot) { }

    /**
     * Rendering method for the body model part.
     * @param pose The given {@link PoseStack}. Comes pre-transformed with the underlying {@link net.minecraft.client.model.PlayerModel}.
     * @param bufferSource The given {@link MultiBufferSource} of the rendering pass.
     * @param packetLight The current light information packed as an {@link Integer}.
     * @param deltaTick Current frame delta since the last rendered frame.
     * @param p The current local {@link Player}.
     * @param stack The {@link ItemStack} causing this renderer to be called.
     * @param slot The {@link BaubleType} of the {@link IBaublesHolder} slot the given {@link ItemStack} is in.
     */
    default void renderHead(PoseStack pose,
                            MultiBufferSource bufferSource,
                            int packetLight,
                            float deltaTick,
                            Player p,
                            ItemStack stack,
                            BaubleType slot) { }

    /**
     * Rendering method for both arm model parts. Called twice, once for each arm.
     * @param pose The given {@link PoseStack}. Comes pre-transformed with the underlying {@link net.minecraft.client.model.PlayerModel}.
     * @param bufferSource The given {@link MultiBufferSource} of the rendering pass.
     * @param packetLight The current light information packed as an {@link Integer}.
     * @param deltaTick Current frame delta since the last rendered frame.
     * @param arm The currently rendered {@link HumanoidArm}.
     * @param isThinModel Whether the current {@link net.minecraft.client.model.PlayerModel} is the thin Alex variant.
     * @param p The current local {@link Player}.
     * @param stack The {@link ItemStack} causing this renderer to be called.
     * @param slot The {@link BaubleType} of the {@link IBaublesHolder} slot the given {@link ItemStack} is in.
     */
    default void renderArm(PoseStack pose,
                            MultiBufferSource bufferSource,
                            int packetLight,
                            float deltaTick,
                            HumanoidArm arm,
                            boolean isThinModel,
                            Player p,
                            ItemStack stack,
                            BaubleType slot) { }

    /**
     * Rendering method for both leg model parts. Called twice, once for each arm.
     * @param pose The given {@link PoseStack}. Comes pre-transformed with the underlying {@link net.minecraft.client.model.PlayerModel}.
     * @param bufferSource The given {@link MultiBufferSource} of the rendering pass.
     * @param packetLight The current light information packed as an {@link Integer}.
     * @param deltaTick Current frame delta since the last rendered frame.
     * @param leg The currently rendered {@link HumanoidLeg}.
     * @param p The current local {@link Player}.
     * @param stack The {@link ItemStack} causing this renderer to be called.
     * @param slot The {@link BaubleType} of the {@link IBaublesHolder} slot the given {@link ItemStack} is in.
     */
    default void renderLeg(PoseStack pose,
                            MultiBufferSource bufferSource,
                            int packetLight,
                            float deltaTick,
                            HumanoidLeg leg,
                            Player p,
                            ItemStack stack,
                            BaubleType slot) { }

    /**
     * A simple utility enum mirroring {@link HumanoidArm} as there is none pre-existing.
     */
    enum HumanoidLeg { LEFT, RIGHT }

    final class Helper {

        /**
         * Utility method converting the standard pixel resolution of Minecraft into a unit value.
         * @param pixels The amount of pixels to turn into a unit value.
         * @return The given amount of pixels converted to a unit value.
         */
        public static float pixelToUnit(float pixels) {
            return 1F / 16 * pixels;
        }

        /**
         * Renders a small red cube centered at the current origin point of the {@link PoseStack}.
         * @param source The given {@link MultiBufferSource}.
         * @param pose The pre-transformed {@link PoseStack}.
         */
        public static void renderPoseOriginMarker(@NotNull MultiBufferSource source, @NotNull PoseStack pose) {
            renderPoseOriginMarker(source, pose, 1F, 0F, 0F, 0.5F);
        }

        /**
         /**
         * Renders a small coloured cube centered at the current origin point of the {@link PoseStack}.
         * @param source The given {@link MultiBufferSource}.
         * @param pose The pre-transformed {@link PoseStack}.
         * @param red The red colour component of the rendered cube, from 0 to 1.
         * @param green The green colour component of the rendered cube, from 0 to 1.
         * @param blue The blue colour component of the rendered cube, from 0 to 1.
         * @param alpha The alpha translucency component of the rendered cube, from 0 to 1.
         */
        public static void renderPoseOriginMarker(@NotNull MultiBufferSource source, @NotNull PoseStack pose, @NonNegative float red, @NonNegative float green, @NonNegative float blue, @NonNegative float alpha) {
            float step = pixelToUnit(0.5F);
            pose.pushPose();
            pose.translate(-step / 2, step / 2, -step / 2);
            VertexConsumer consumer = source.getBuffer(RenderType.debugFilledBox());
			ShapeRenderer.addChainedFilledBoxVertices(pose, consumer, step, -step, step, 0, 0, 0, red, green, blue, alpha);
            pose.popPose();
        }
    }
}
