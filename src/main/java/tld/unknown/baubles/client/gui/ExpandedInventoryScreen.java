package tld.unknown.baubles.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.CraftingRecipeBookComponent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import tld.unknown.baubles.api.Baubles;
import tld.unknown.baubles.menu.ExpandedInventoryMenu;

public class ExpandedInventoryScreen extends AbstractRecipeBookScreen<ExpandedInventoryMenu> {

    private float xMouse, yMouse;
    private boolean buttonClicked;

	private final EffectsInInventory effects;

    public ExpandedInventoryScreen(ExpandedInventoryMenu inv, Player p, Component name) {
        super(inv, new CraftingRecipeBookComponent(p.inventoryMenu), p.getInventory(), name);
        this.titleLabelX = 115;
        this.titleLabelY = 8;
		this.effects = new EffectsInInventory(this);
    }

	@Override
	protected ScreenPosition getRecipeBookButtonPosition() {
		return new ScreenPosition(this.leftPos + 124, this.height / 2 - 22);
	}

	@Override
	protected void onRecipeBookButtonClick() {
		this.buttonClicked = true;
	}


	@Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		this.effects.render(pGuiGraphics, pMouseX, pMouseY);
        this.xMouse = (float)pMouseX;
        this.yMouse = (float)pMouseY;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = this.leftPos;
        int y = this.topPos;
		pGuiGraphics.blit(RenderPipelines.GUI_TEXTURED, Baubles.Textures.UI_EXPANDED_INV, x, y, 0, 0, this.imageWidth, this.imageHeight, 176, 166);
        InventoryScreen.renderEntityInInventoryFollowsMouse(pGuiGraphics, x + 26, y + 8, x + 75, y + 78, 30, 0.0625F, this.xMouse, this.yMouse, this.minecraft.player);
    }

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		if (this.buttonClicked) {
			this.buttonClicked = false;
			return true;
		} else {
			return super.mouseReleased(event);
		}
	}
}
