package tld.unknown.baubles.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
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

    private boolean buttonClicked;

	private final EffectsInInventory effects;

    public ExpandedInventoryScreen(ExpandedInventoryMenu inv, Player p, Component name) {
        super(inv, new CraftingRecipeBookComponent(p.inventoryMenu), p.getInventory(), name);
        this.titleLabelX = 115;
        this.titleLabelY = 6;
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
	protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
		graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);

	}

	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractContents(graphics, mouseX, mouseY, a);
		this.effects.extractRenderState(graphics, mouseX, mouseY);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		int x = this.leftPos;
		int y = this.topPos;
		graphics.blit(RenderPipelines.GUI_TEXTURED, Baubles.Textures.UI_EXPANDED_INV, x, y, 0, 0, this.imageWidth, this.imageHeight, 176, 166);

		float centerX = (x + 26 + x + 75) / 2.0F;
		float centerY = (y + 8 + y + 78) / 2.0F;
		float xAngle = (float)Math.atan((centerX - mouseX) / 40.0F);
		float yAngle = (float)Math.atan((centerY - mouseY) / 40.0F);
		InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, x + 26, y + 8, x + 75, y + 78, 30, 0.0625F, xAngle, yAngle, this.minecraft.player);

		InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, x + 26, y + 8, x + 75, y + 78, 30, 0.0625F, mouseX, mouseY, this.minecraft.player);
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
