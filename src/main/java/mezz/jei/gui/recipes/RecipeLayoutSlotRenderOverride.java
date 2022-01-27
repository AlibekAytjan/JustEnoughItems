package mezz.jei.gui.recipes;

import mezz.jei.api.ingredients.IIngredientRenderer;

public class RecipeLayoutSlotRenderOverride<T> {
	private final IIngredientRenderer<T> ingredientRenderer;
	private final int width;
	private final int height;
	private final int xPadding;
	private final int yPadding;

	public RecipeLayoutSlotRenderOverride(IIngredientRenderer<T> ingredientRenderer, int width, int height, int xPadding, int yPadding) {
		this.ingredientRenderer = ingredientRenderer;
		this.width = width;
		this.height = height;
		this.xPadding = xPadding;
		this.yPadding = yPadding;
	}

	public IIngredientRenderer<T> getIngredientRenderer() {
		return ingredientRenderer;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getXPadding() {
		return xPadding;
	}

	public int getYPadding() {
		return yPadding;
	}
}
