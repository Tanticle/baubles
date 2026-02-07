package tld.unknown.baubles.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import tld.unknown.baubles.BaublesMod;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.Baubles;
import tld.unknown.baubles.api.IBaubleRenderer;
import tld.unknown.baubles.client.BaublesClient;

public class BaublesRenderLayer extends RenderLayer<PlayerRenderState, PlayerModel> {

    private static final float BODY_OFFSET = 0.75F;

    public BaublesRenderLayer(RenderLayerParent<PlayerRenderState, PlayerModel> pRenderer) {
        super(pRenderer);
    }

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, PlayerRenderState renderState, float yRot, float xRot) {
		if(renderState.isInvisible || renderState.isInvisibleToPlayer)
			return;

		Player player = Minecraft.getInstance().player;
		float pPartialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();

		ItemStack[] holder = player.getData(BaublesMod.ATTACHMENT_BAUBLES).getAllSlots();
		for (int i = 0; i < holder.length; i++) {
			ItemStack item = holder[i];
			if(item == ItemStack.EMPTY || !Baubles.API.isBaubleItem(item))
				continue;
			IBaubleRenderer renderer = BaublesClient.RENDERERS.getRenderer(item);
			if(renderer == null)
				continue;

			BaubleType slot = BaubleType.bySlotId(i);

			// Head
			poseStack.pushPose();
			ModelPart head = getParentModel().head;
			translateAndRotate(poseStack, head);
			if(isDebugRendering())
				IBaubleRenderer.Helper.renderPoseOriginMarker(bufferSource, poseStack, 1F, 0F, 0F, 0.5F);
			renderer.renderHead(poseStack, bufferSource, packedLight, pPartialTick, player, item, slot);
			poseStack.popPose();
			// Body
			poseStack.pushPose();
			ModelPart body = getParentModel().body;
			translateAndRotate(poseStack, body);
			poseStack.translate(0, BODY_OFFSET, 0);
			if(isDebugRendering())
				IBaubleRenderer.Helper.renderPoseOriginMarker(bufferSource, poseStack, 1F, 0F, 0F, 0.5F);
			renderer.renderBody(poseStack, bufferSource, packedLight, pPartialTick, player, item, slot);
			poseStack.popPose();

			// Arms
			dispatchArm(poseStack, bufferSource, packedLight, pPartialTick, player, HumanoidArm.LEFT, renderer, item, slot);
			dispatchArm(poseStack, bufferSource, packedLight, pPartialTick, player, HumanoidArm.RIGHT, renderer, item, slot);

			// Legs
			dispatchLeg(poseStack, bufferSource, packedLight, pPartialTick, player, IBaubleRenderer.HumanoidLeg.LEFT, renderer, item, slot);
			dispatchLeg(poseStack, bufferSource, packedLight, pPartialTick, player, IBaubleRenderer.HumanoidLeg.RIGHT, renderer, item, slot);
		}
	}

    private static final float ARM_Y_OFFSET = IBaubleRenderer.Helper.pixelToUnit(10);
    private static final float THIN_ARM_X_OFFSET = IBaubleRenderer.Helper.pixelToUnit(0.5F);
    private static final float THICK_ARM_X_OFFSET = IBaubleRenderer.Helper.pixelToUnit(1F);

    private void dispatchArm(PoseStack pose, MultiBufferSource buffer, int packedLight, float delta, Player player, HumanoidArm arm, IBaubleRenderer renderer, ItemStack item, BaubleType type) {
        ModelPart part = arm == HumanoidArm.RIGHT ? getParentModel().rightArm : getParentModel().leftArm;
        boolean isThin = ((AbstractClientPlayer)player).getSkin().model() == PlayerSkin.Model.SLIM;
        pose.pushPose();
        translateAndRotate(pose, part);
        float xOffset = isThin ? (arm == HumanoidArm.RIGHT ? -THIN_ARM_X_OFFSET : THIN_ARM_X_OFFSET) : (arm == HumanoidArm.RIGHT ? -THICK_ARM_X_OFFSET : THICK_ARM_X_OFFSET);
        pose.translate(xOffset, ARM_Y_OFFSET, 0);
        if(isDebugRendering())
            IBaubleRenderer.Helper.renderPoseOriginMarker(buffer, pose, 1F, 0F, 0F, 0.5F);
        renderer.renderArm(pose, buffer, packedLight, delta, arm, isThin, player, item, type);
        pose.popPose();
    }

    private static final float LEG_Y_OFFSET = IBaubleRenderer.Helper.pixelToUnit(12);

    private void dispatchLeg(PoseStack pose, MultiBufferSource buffer, int packedLight, float delta, Player player, IBaubleRenderer.HumanoidLeg leg, IBaubleRenderer renderer, ItemStack item, BaubleType type) {
        ModelPart part = leg == IBaubleRenderer.HumanoidLeg.RIGHT ? getParentModel().rightLeg : getParentModel().leftLeg;
        pose.pushPose();
        translateAndRotate(pose, part);
        pose.translate(0, LEG_Y_OFFSET, 0);
        if(isDebugRendering())
            IBaubleRenderer.Helper.renderPoseOriginMarker(buffer, pose, 1F, 0F, 0F, 0.5F);
        renderer.renderLeg(pose, buffer, packedLight, delta, leg, player, item, type);
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
