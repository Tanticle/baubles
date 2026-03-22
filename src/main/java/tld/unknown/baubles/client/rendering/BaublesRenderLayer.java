package tld.unknown.baubles.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelType;
import org.joml.Quaternionf;
import tld.unknown.baubles.api.BaubleRenderContext;
import tld.unknown.baubles.api.Baubles;
import tld.unknown.baubles.api.IBaubleRenderer;
import tld.unknown.baubles.client.BaublesClient;

public class BaublesRenderLayer extends RenderLayer<AvatarRenderState, PlayerModel> {

    private static final float BODY_OFFSET = 0.75F;

    public BaublesRenderLayer(RenderLayerParent<AvatarRenderState, PlayerModel> pRenderer) {
        super(pRenderer);
    }

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
		if(state.isInvisible || state.isInvisibleToPlayer)
			return;
		render(poseStack, submitNodeCollector, state);
	}

	@SuppressWarnings("unchecked")
	private <CTX extends BaubleRenderContext> void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, AvatarRenderState state) {
		state.getRenderData(Baubles.CONTEXT_BAUBLES).forEach((pair) -> {
			IBaubleRenderer<CTX> renderer = (IBaubleRenderer<CTX>)BaublesClient.RENDERERS.getRenderer(pair.getFirst());
			CTX context = (CTX)pair.getSecond();

			// Head
			poseStack.pushPose();
			ModelPart head = getParentModel().head;
			translateAndRotate(poseStack, head);
			if(isDebugRendering())
				IBaubleRenderer.Helper.renderPoseOriginMarker(submitNodeCollector, poseStack);
			renderer.renderHead(poseStack, submitNodeCollector, state, context);
			poseStack.popPose();

			// Body
			poseStack.pushPose();
			ModelPart body = getParentModel().body;
			translateAndRotate(poseStack, body);
			poseStack.translate(0, BODY_OFFSET, 0);
			if(isDebugRendering())
				IBaubleRenderer.Helper.renderPoseOriginMarker(submitNodeCollector, poseStack);
			renderer.renderBody(poseStack, submitNodeCollector, state, context);
			poseStack.popPose();

			// Arms
			dispatchArm(poseStack, submitNodeCollector, renderer, state, context, HumanoidArm.LEFT);
			dispatchArm(poseStack, submitNodeCollector, renderer, state, context, HumanoidArm.RIGHT);

			// Legs
			dispatchLeg(poseStack, submitNodeCollector, renderer, state, context, IBaubleRenderer.HumanoidLeg.LEFT);
			dispatchLeg(poseStack, submitNodeCollector, renderer, state, context, IBaubleRenderer.HumanoidLeg.RIGHT);
		});
	}

    private static final float ARM_Y_OFFSET = IBaubleRenderer.Helper.pixelToUnit(10);
    private static final float THIN_ARM_X_OFFSET = IBaubleRenderer.Helper.pixelToUnit(0.5F);
    private static final float THICK_ARM_X_OFFSET = IBaubleRenderer.Helper.pixelToUnit(1F);

    private <CTX extends BaubleRenderContext> void dispatchArm(PoseStack pose, SubmitNodeCollector collector, IBaubleRenderer<CTX> renderer, AvatarRenderState avatarState, CTX context, HumanoidArm arm) {
        ModelPart part = arm == HumanoidArm.RIGHT ? getParentModel().rightArm : getParentModel().leftArm;
        pose.pushPose();
        translateAndRotate(pose, part);
		boolean isThin = avatarState.skin.model() == PlayerModelType.SLIM;
        float xOffset = isThin ? (arm == HumanoidArm.RIGHT ? -THIN_ARM_X_OFFSET : THIN_ARM_X_OFFSET) : (arm == HumanoidArm.RIGHT ? -THICK_ARM_X_OFFSET : THICK_ARM_X_OFFSET);
        pose.translate(xOffset, ARM_Y_OFFSET, 0);
        if(isDebugRendering())
            IBaubleRenderer.Helper.renderPoseOriginMarker(collector, pose);
        renderer.renderArm(pose, collector, avatarState, context, arm, isThin);
        pose.popPose();
    }

    private static final float LEG_Y_OFFSET = IBaubleRenderer.Helper.pixelToUnit(12);

    private <CTX extends BaubleRenderContext> void dispatchLeg(PoseStack pose, SubmitNodeCollector collector, IBaubleRenderer<CTX> renderer, AvatarRenderState avatarState, CTX context, IBaubleRenderer.HumanoidLeg leg) {
        ModelPart part = leg == IBaubleRenderer.HumanoidLeg.RIGHT ? getParentModel().rightLeg : getParentModel().leftLeg;
        pose.pushPose();
        translateAndRotate(pose, part);
        pose.translate(0, LEG_Y_OFFSET, 0);
        if(isDebugRendering())
            IBaubleRenderer.Helper.renderPoseOriginMarker(collector, pose);
        renderer.renderLeg(pose, collector, avatarState, context, leg);
        pose.popPose();
    }

    private void translateAndRotate(PoseStack pose, ModelPart part) {
        pose.translate(part.x / 16.0F, part.y / 16.0F, part.z / 16.0F);
        if (part.xRot != 0.0F || part.yRot != 0.0F || part.zRot != 0.0F) {
            pose.mulPose(new Quaternionf().rotationZYX(part.zRot, part.yRot, part.xRot));
        }
    }

    private boolean isDebugRendering() {
        return BaublesClient.RENDERERS.renderDebugMode;
    }
}
