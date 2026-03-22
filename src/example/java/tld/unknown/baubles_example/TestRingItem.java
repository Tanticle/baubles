package tld.unknown.baubles_example;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tld.unknown.baubles.api.BaubleRenderContext;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.IBauble;
import tld.unknown.baubles.api.IBaubleRenderer;

import java.awt.*;

public class TestRingItem extends Item implements IBauble {

    public TestRingItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void onEquipped(BaubleType type, ItemStack stack, Player player) {
        String side = player.level().isClientSide() ? "Client" : "Server";
        System.out.printf("Equipped a %s on the %s.%n", type.name(), side);
    }

    @Override
    public void onUnequipped(BaubleType type, ItemStack stack, Player player) {
        String side = player.level().isClientSide() ? "Client" : "Server";
        System.out.printf("Unequipped a %s on the %s.%n", type.name(), side);
    }

	public static class TestRenderer implements IBaubleRenderer<BaubleRenderContext> {

        @Override
        public void renderHead(PoseStack poseStack, SubmitNodeCollector nodeCollector, AvatarRenderState avatarState, BaubleRenderContext ctx) {
            if(ctx.baubleType() == BaubleType.HEAD) {
                float width = Helper.pixelToUnit(8.5F);
                float downOffset = Helper.pixelToUnit(0.25F);
                poseStack.translate(-width / 2, downOffset, -width / 2);
				nodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugFilledBox(), (pose, consumer) -> {
                    Helper.drawCuboid(consumer, pose, new Color(1F, 1F, 1F, .5F).getRGB(), width, -width, width);
				});
            }
        }

        @Override
        public void renderBody(PoseStack poseStack, SubmitNodeCollector nodeCollector,AvatarRenderState avatarState, BaubleRenderContext ctx) {
            if (ctx.baubleType() == BaubleType.AMULET || ctx.baubleType() == BaubleType.BODY) {
                float width = Helper.pixelToUnit(8.5F);
                float depth = Helper.pixelToUnit(4.5F);
                float height = Helper.pixelToUnit(12.5F);
                float downOffset = Helper.pixelToUnit(0.25F);
                poseStack.translate(-width / 2, downOffset, -depth / 2);
				nodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugFilledBox(), (pose, consumer) -> {
					Helper.drawCuboid(consumer, pose, new Color(1F, 1F, 1F, .5F).getRGB(), width, -height, depth);
				});
            }
        }

        @Override
        public void renderArm(PoseStack pose, SubmitNodeCollector nodeCollector, AvatarRenderState avatarState, BaubleRenderContext ctx, HumanoidArm arm, boolean isThin) {
            if((ctx.baubleType() == BaubleType.RING_LEFT && arm == HumanoidArm.LEFT) || (ctx.baubleType() == BaubleType.RING_RIGHT && arm == HumanoidArm.RIGHT)) {
                renderModelPart(nodeCollector, pose, Helper.pixelToUnit(isThin ? 3.5F : 4.5F));
            }
        }

        @Override
        public void renderLeg(PoseStack pose, SubmitNodeCollector nodeCollector, AvatarRenderState avatarState, BaubleRenderContext ctx, HumanoidLeg leg) {
            if(ctx.baubleType() == BaubleType.BELT) {
                renderModelPart(nodeCollector, pose, Helper.pixelToUnit(4.5F));
            }
        }

        private void renderModelPart(SubmitNodeCollector nodeCollector, PoseStack poseStack, float width) {
            float depth = Helper.pixelToUnit(4.5F);
            float height = Helper.pixelToUnit(12.5F);
            float downOffset = Helper.pixelToUnit(0.25F);
            poseStack.translate(-width / 2, downOffset, -depth / 2);
			nodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugFilledBox(), (pose, consumer) -> {
				Helper.drawCuboid(consumer, pose, new Color(1F, 1F, 1F, .5F).getRGB(), width, -height, depth);
			});
        }

        @Override
        public BaubleRenderContext prepareRenderState(ItemStack stack, BaubleType slot, Avatar avatar, Level level) {
            return new TestItemRenderContext(stack, slot);
        }
    }

    public static final class TestItemRenderContext extends BaubleRenderContext {

        public final int colour;

        public TestItemRenderContext(ItemStack baubleStack, BaubleType baubleType) {
            super(baubleStack, baubleType);
            this.colour = new Color(1F, 1F, 1F, .5F).getRGB();
        }
    }
}
