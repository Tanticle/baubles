package tld.unknown.baubles.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
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
}
