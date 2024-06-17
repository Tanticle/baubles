package tld.unknown.baubles_example;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import tld.unknown.baubles.api.BaubleType;
import tld.unknown.baubles.api.IBauble;
import tld.unknown.baubles.api.IBaubleRenderer;

public class TestRingItem extends Item implements IBauble {

    public TestRingItem() {
        super(new Properties().stacksTo(1));
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

    public static class TestRenderer implements IBaubleRenderer {

        @Override
        public void renderHead(PoseStack pose, MultiBufferSource bufferSource, int packetLight, float deltaTick, Player p, ItemStack stack, BaubleType slot) {
            if(slot == BaubleType.HEAD) {
                float width = pixelToUnit(8.5F);
                float downOffset = pixelToUnit(0.25F);
                pose.translate(-width / 2, downOffset, -width / 2);
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugFilledBox());
                LevelRenderer.addChainedFilledBoxVertices(pose, consumer, 0, 0, 0, width, -width, width, 1F, 1F, 1F, 0.5F);
            }
        }

        @Override
        public void renderBody(PoseStack pose, MultiBufferSource bufferSource, int packetLight, float deltaTick, Player p, ItemStack stack, BaubleType slot) {
            if (slot == BaubleType.BODY) {
                float width = pixelToUnit(8.5F);
                float depth = pixelToUnit(4.5F);
                float height = pixelToUnit(12.5F);
                float downOffset = pixelToUnit(0.25F);
                pose.translate(-width / 2, downOffset, -depth / 2);
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugFilledBox());
                LevelRenderer.addChainedFilledBoxVertices(pose, consumer, 0, 0, 0, width, -height, depth, 1F, 1F, 1F, 0.5F);
            }
        }

        @Override
        public void renderArm(PoseStack pose, MultiBufferSource bufferSource, int packetLight, float deltaTick, HumanoidArm arm, Player p, ItemStack stack, BaubleType slot) {
            if(slot == BaubleType.RING_LEFT && arm == HumanoidArm.LEFT) {
                float width = pixelToUnit(4.5F);
                float height = pixelToUnit(12.5F);
                float downOffset = pixelToUnit(0.25F);
                pose.translate(-width / 2, downOffset, -width / 2);
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugFilledBox());
                LevelRenderer.addChainedFilledBoxVertices(pose, consumer, 0, 0, 0, width, -height, width, 1F, 1F, 1F, 0.5F);
            }

            if(slot == BaubleType.RING_RIGHT && arm == HumanoidArm.RIGHT) {
                float width = pixelToUnit(4.5F);
                float height = pixelToUnit(12.5F);
                float downOffset = pixelToUnit(0.25F);
                pose.translate(-width / 2, downOffset, -width / 2);
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugFilledBox());
                LevelRenderer.addChainedFilledBoxVertices(pose, consumer, 0, 0, 0, width, -height, width, 1F, 1F, 1F, 0.5F);
            }
        }

        @Override
        public void renderLeg(PoseStack pose, MultiBufferSource bufferSource, int packetLight, float deltaTick, HumanoidLeg leg, Player p, ItemStack stack, BaubleType slot) {
            if(slot == BaubleType.BELT && leg == HumanoidLeg.LEFT) {
                float width = pixelToUnit(4.5F);
                float height = pixelToUnit(12.5F);
                float downOffset = pixelToUnit(0.25F);
                pose.translate(-width / 2, downOffset, -width / 2);
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugFilledBox());
                LevelRenderer.addChainedFilledBoxVertices(pose, consumer, 0, 0, 0, width, -height, width, 1F, 1F, 1F, 0.5F);
            }

            if(slot == BaubleType.BELT && leg == HumanoidLeg.RIGHT) {
                float width = pixelToUnit(4.5F);
                float height = pixelToUnit(12.5F);
                float downOffset = pixelToUnit(0.25F);
                pose.translate(-width / 2, downOffset, -width / 2);
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugFilledBox());
                LevelRenderer.addChainedFilledBoxVertices(pose, consumer, 0, 0, 0, width, -height, width, 1F, 1F, 1F, 0.5F);
            }
        }

        private float pixelToUnit(float pixels) {
            return 1F / 16 * pixels;
        }
    }
}
