package tld.unknown.baubles.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import tld.unknown.baubles.api.Baubles;
import tld.unknown.baubles.networking.ServerboundOpenBaublesInvPacket;

public class BaublesButton extends AbstractButton {

    private static final Component TEXT_NORMAL = Component.translatable("button.baubles.normal");
    private static final Component TEXT_BAUBLES = Component.translatable("button.baubles.baubles");

    private final AbstractContainerScreen<?> parent;

    public BaublesButton(AbstractContainerScreen<?> parent, int pX, int pY, boolean isBauble) {
        super(pX + parent.getGuiLeft(), pY + parent.getGuiTop(), 10, 10, isBauble ? TEXT_NORMAL : TEXT_BAUBLES );
        this.parent = parent;
    }

	@Override
	public void onPress(InputWithModifiers input) {
		if(parent instanceof ExpandedInventoryScreen) {
			Minecraft.getInstance().setScreen(new InventoryScreen(Minecraft.getInstance().player));
		} else {
			float x = (float)Minecraft.getInstance().mouseHandler.xpos();
			float y = (float)Minecraft.getInstance().mouseHandler.ypos();
			Minecraft.getInstance().getConnection().send(new ServerboundOpenBaublesInvPacket(x, y));
		}
	}

	@Override
	protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
		if (this.visible) {
			graphics.pose().pushMatrix();
			if (isHovered()) {
				graphics.blit(RenderPipelines.GUI_TEXTURED, Baubles.Textures.UI_BAUBLES_BUTTON, getX(), getY(), 10, 0, 10, 10, 20, 10);
				graphics.drawCenteredString(Minecraft.getInstance().font, getMessage(), getX() + 5, getY() + getHeight(), 0xFFFFFF);
			} else {
				graphics.blit(RenderPipelines.GUI_TEXTURED,Baubles.Textures.UI_BAUBLES_BUTTON, getX(), getY(), 0, 0, 10, 10, 20, 10);
			}
			graphics.pose().popMatrix();
		}
	}

	@Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput);
    }
}