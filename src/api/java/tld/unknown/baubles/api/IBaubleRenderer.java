package tld.unknown.baubles.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IBaubleRenderer {

    default void renderBody(PoseStack pose,
                            MultiBufferSource bufferSource,
                            int packetLight,
                            float deltaTick,
                            Player p,
                            ItemStack stack,
                            BaubleType slot) { }

    default void renderHead(PoseStack pose,
                            MultiBufferSource bufferSource,
                            int packetLight,
                            float deltaTick,
                            Player p,
                            ItemStack stack,
                            BaubleType slot) { }

    default void renderArm(PoseStack pose,
                            MultiBufferSource bufferSource,
                            int packetLight,
                            float deltaTick,
                            HumanoidArm arm,
                            boolean isThinModel,
                            Player p,
                            ItemStack stack,
                            BaubleType slot) { }

    default void renderLeg(PoseStack pose,
                            MultiBufferSource bufferSource,
                            int packetLight,
                            float deltaTick,
                            HumanoidLeg leg,
                            Player p,
                            ItemStack stack,
                            BaubleType slot) { }

    enum HumanoidLeg { LEFT, RIGHT }

    final class Helper {

        public static float pixelToUnit(float pixels) {
            return 1F / 16 * pixels;
        }

        public static void renderPoseOriginMarker(MultiBufferSource source, PoseStack pose) {
            renderPoseOriginMarker(source, pose, 1F, 0F, 0F, 0.5F);
        }

        public static void renderPoseOriginMarker(MultiBufferSource source, PoseStack pose, float red, float green, float blue, float alpha) {
            float step = pixelToUnit(0.5F);
            pose.pushPose();
            pose.translate(-step / 2, step / 2, -step / 2);
            VertexConsumer consumer = source.getBuffer(RenderType.debugFilledBox());
            LevelRenderer.addChainedFilledBoxVertices(pose, consumer, step, -step, step, 0, 0, 0, red, green, blue, alpha);
            pose.popPose();
        }
    }
}
