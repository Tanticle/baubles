package tld.unknown.baubles.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.awt.*;

/**
 * The renderer interface for an {@link IBauble} implementation. Must be registered via the {@link BaublesEvent.RendererRegistration} event.
 * @see <a href=https://github.com/Tanticle/baubles/wiki/Rendering-Your-Bauble>Baubles 2 Wiki on Rendering</a>
 * @author Tom Tanticle
 */
public interface IBaubleRenderer<CTX extends BaubleRenderContext> {

	/**
	 * Constructor method for the {@link BaubleRenderContext} for this renderer. Any custom data regarding the given
	 * {@link Avatar} entity, the {@link Level}, the used {@link BaubleType} or the given bauble {@link ItemStack}. <br><br>
	 * When needing data beyond the {@link BaubleType} or the bauble {@link ItemStack}, one should implement the
	 * {@link BaubleRenderContext} class in an object of their own to create, populate and return it here. It is then passed to the
	 * individual rendering methods. Alternatively, if no additional information is needed,
	 * {@link BaubleRenderContext#create(ItemStack, BaubleType)} may be used for a default implementation.
	 * @param stack The {@link ItemStack} of the bauble currently being renderer.
	 * @param slot The {@link BaubleType} slot currently being renderer.
	 * @param avatar The current {@link Avatar} entity currently being renderer. Will be a {@link Player} or a {@link net.minecraft.world.entity.decoration.Mannequin}.
	 * @param level THe currently active {@link Level} being renderer.
	 * @return A custom or the default implementation of {@link BaubleRenderContext}.
	 */
	CTX prepareRenderState(ItemStack stack, BaubleType slot, Avatar avatar, Level level);

    /**
     * Rendering method for the head model part.
     * @param pose The given {@link PoseStack}. Comes pre-transformed with the underlying {@link net.minecraft.world.entity.player.PlayerModelType}.
     * @param nodeCollector The given {@link SubmitNodeCollector} of the rendering pass.
	 * @param avatarState The {@link AvatarRenderState} of the rendering {@link Avatar} entity.
	 * @param context The implementation of {@link BaubleRenderContext} for this renderer.
     */
    default void renderBody(PoseStack pose,
							SubmitNodeCollector nodeCollector,
							AvatarRenderState avatarState,
							CTX context) { }

    /**
     * Rendering method for the body model part.
     * @param pose The given {@link PoseStack}. Comes pre-transformed with the underlying {@link net.minecraft.world.entity.player.PlayerModelType}.
     * @param nodeCollector The given {@link SubmitNodeCollector} of the rendering pass.
	 * @param avatarState The {@link AvatarRenderState} of the rendering {@link Avatar} entity.
	 * @param context The implementation of {@link BaubleRenderContext} for this renderer.
     */
    default void renderHead(PoseStack pose,
							SubmitNodeCollector nodeCollector,
							AvatarRenderState avatarState,
							CTX context) { }

    /**
     * Rendering method for both arm model parts. Called twice, once for each arm.
     * @param pose The given {@link PoseStack}. Comes pre-transformed with the underlying {@link net.minecraft.world.entity.player.PlayerModelType}.
     * @param nodeCollector The given {@link SubmitNodeCollector} of the rendering pass.
	 * @param avatarState The {@link AvatarRenderState} of the rendering {@link Avatar} entity.
	 * @param context The implementation of {@link BaubleRenderContext} for this renderer.
     * @param arm The currently rendered {@link HumanoidArm}.
     * @param isThinModel Whether the current {@link net.minecraft.world.entity.player.PlayerModelType} is the thin Alex variant.
     */
    default void renderArm(PoseStack pose,
                            SubmitNodeCollector nodeCollector,
						   	AvatarRenderState avatarState,
						   	CTX context,
                            HumanoidArm arm,
                            boolean isThinModel) { }

    /**
     * Rendering method for both leg model parts. Called twice, once for each arm.
     * @param pose The given {@link PoseStack}. Comes pre-transformed with the underlying {@link net.minecraft.world.entity.player.PlayerModelType}.
     * @param nodeCollector The given {@link SubmitNodeCollector} of the rendering pass.
	 * @param avatarState The {@link AvatarRenderState} of the rendering {@link Avatar} entity.
	 * @param context The implementation of {@link BaubleRenderContext} for this renderer.
     * @param leg The currently rendered {@link HumanoidLeg}.
     */
    default void renderLeg(PoseStack pose,
                            SubmitNodeCollector nodeCollector,
						   	AvatarRenderState avatarState,
						   	CTX context,
                            HumanoidLeg leg) { }

    /**
     * A simple utility enum mirroring {@link HumanoidArm} as there is none pre-existing.
     */
    enum HumanoidLeg { LEFT, RIGHT }

    final class Helper {

        /**
         * Utility method converting the standard pixel resolution of Minecraft into a unit value.
         * @param pixels The amount of pixels to turn into a unit value.
         * @return The given amount of pixels converted to a unit value.
		 * */
        public static float pixelToUnit(float pixels) {
            return 1F / 16 * pixels;
        }

		/**
         * Renders a small red cube centered at the current origin point of the {@link PoseStack}.
         * @param collector The provided {@link SubmitNodeCollector}.
         * @param stack The pre-transformed {@link PoseStack}.
         */
        public static void renderPoseOriginMarker(@NotNull SubmitNodeCollector collector, @NotNull PoseStack stack) {
            float step = pixelToUnit(0.5F);
			stack.pushPose();
			stack.translate(-step / 2, step / 2, -step / 2);
			collector.submitCustomGeometry(stack, RenderTypes.debugFilledBox(), (pose, consumer) -> {
				drawCuboid(consumer, pose, Color.RED.getRGB(), step, step, step);
			});
			stack.popPose();
        }

		/**
		 * Renders a cuboid centered at the position defined by the {@link com.mojang.blaze3d.vertex.PoseStack.Pose} matrix.
		 * Needs to be called from within a {@link SubmitNodeCollector#submitCustomGeometry(PoseStack, RenderType, SubmitNodeCollector.CustomGeometryRenderer)} call.
		 * @param consumer The provided {@link VertexConsumer}.
		 * @param modelMatrix The pre-transformed {@link com.mojang.blaze3d.vertex.PoseStack.Pose} matrix.
		 * @param colour The vertex colour being passed to the rendered vertices as a packed 32-bit RGBA integer.
		 * @param width The width of the cuboid to be rendered.
		 * @param height The height of the cuboid to be rendered.
		 * @param depth The depth of the cuboid to be rendered.
		 */
		public static void drawCuboid(VertexConsumer consumer, PoseStack.Pose modelMatrix, int colour, float width, float height, float depth) {
			Vector3f bfl = new Vector3f(0, 0, 0);
			Vector3f bfr = new Vector3f(width, 0, 0);
			Vector3f bbl = new Vector3f(0, 0, depth);
			Vector3f bbr = new Vector3f(width, 0, depth);
			Vector3f tfl = new Vector3f(0, height, 0);
			Vector3f tfr = new Vector3f(width, height, 0);
			Vector3f tbl = new Vector3f(0, height, depth);
			Vector3f tbr = new Vector3f(width, height, depth);
			drawFace(Direction.NORTH, consumer, modelMatrix, bfl, tfr, colour);
			drawFace(Direction.SOUTH, consumer, modelMatrix, bbl, tbr, colour);
			drawFace(Direction.EAST, consumer, modelMatrix, bfr, tbr, colour);
			drawFace(Direction.WEST, consumer, modelMatrix, bfl, tbl, colour);
			drawFace(Direction.UP, consumer, modelMatrix, tfl, tbr, colour);
			drawFace(Direction.DOWN, consumer, modelMatrix, bfl, bbr, colour);
		}

		/**
		 * Renders a quad centered at the position defined by the {@link com.mojang.blaze3d.vertex.PoseStack.Pose} matrix and pointing towards the given {@link Direction}.
		 * Needs to be called from within a {@link SubmitNodeCollector#submitCustomGeometry(PoseStack, RenderType, SubmitNodeCollector.CustomGeometryRenderer)} call.
		 * @param dir The given {@link Direction} that the face is pointing towards.
		 * @param consumer The provided {@link VertexConsumer}.
		 * @param modelMatrix The pre-transformed {@link com.mojang.blaze3d.vertex.PoseStack.Pose} matrix.
		 * @param min The minimum vertex position of the face being rendered.
		 * @param max The maximum vertex position of the face being rendered.
		 * @param colour The vertex colour being passed to the rendered vertices as a packed 32-bit RGBA integer.
		 */
		public static void drawFace(Direction dir, VertexConsumer consumer, PoseStack.Pose modelMatrix, Vector3f min, Vector3f max, int colour) {
			switch(dir) {
				case UP -> {
					fillVertex(consumer, modelMatrix, max.x(), max.y(), min.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), max.y(), min.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), max.y(), max.z(), colour);
					fillVertex(consumer, modelMatrix, max.x(), max.y(), max.z(), colour);
				}
				case DOWN -> {
					fillVertex(consumer, modelMatrix, max.x(), min.y(), min.z(), colour);
					fillVertex(consumer, modelMatrix, max.x(), min.y(), max.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), min.y(), max.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), min.y(), min.z(), colour);
				}
				case NORTH -> {
					fillVertex(consumer, modelMatrix, max.x(), min.y(), min.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), min.y(), min.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), max.y(), min.z(), colour);
					fillVertex(consumer, modelMatrix, max.x(), max.y(), min.z(), colour);
				}
				case SOUTH -> {
					fillVertex(consumer, modelMatrix, max.x(), min.y(), max.z(), colour);
					fillVertex(consumer, modelMatrix, max.x(), max.y(), max.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), max.y(), max.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), min.y(), max.z(), colour);
				}
				case EAST -> {
					fillVertex(consumer, modelMatrix, max.x(), min.y(), max.z(), colour);
					fillVertex(consumer, modelMatrix, max.x(), min.y(), min.z(), colour);
					fillVertex(consumer, modelMatrix, max.x(), max.y(), min.z(), colour);
					fillVertex(consumer, modelMatrix, max.x(), max.y(), max.z(), colour);
				}
				case WEST -> {
					fillVertex(consumer, modelMatrix, min.x(), min.y(), max.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), max.y(), max.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), max.y(), min.z(), colour);
					fillVertex(consumer, modelMatrix, min.x(), min.y(), min.z(), colour);
				}
			}
		}

		private static void fillVertex(VertexConsumer consumer, PoseStack.Pose modelMatrix, float x, float y, float z, int colour) {
			consumer.addVertex(modelMatrix, x, y, z).setColor(colour).setNormal(0, 0, 1);
		}
    }
}
