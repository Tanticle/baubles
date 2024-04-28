package tld.unknown.baubles.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import tld.unknown.baubles.api.BaublesData;
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
    public void onPress() {
        if(parent instanceof ExpandedInventoryScreen) {
            Minecraft.getInstance().setScreen(new InventoryScreen(Minecraft.getInstance().player));
        } else {
            float x = (float)Minecraft.getInstance().mouseHandler.xpos();
            float y = (float)Minecraft.getInstance().mouseHandler.ypos();
            Minecraft.getInstance().getConnection().send(new ServerboundOpenBaublesInvPacket(x, y));
        }
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.visible) {
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0, 0, 200);
            if (isHovered()) {
                pGuiGraphics.blit(BaublesData.Textures.UI_BAUBLES_BUTTON, getX(), getY(), 10, 0, 10, 10, 20, 10);
                pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, getMessage(), getX() + 5, getY() + getHeight(), 0xFFFFFF);
            } else {
                pGuiGraphics.blit(BaublesData.Textures.UI_BAUBLES_BUTTON, getX(), getY(), 0, 0, 10, 10, 20, 10);
            }
            pGuiGraphics.pose().popPose();
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput);
    }
}