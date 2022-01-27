package mezz.jei.api.gui.builder;

import mezz.jei.api.recipe.RecipeIngredientRole;

public interface IRecipeLayoutBuilder {
	IRecipeLayoutSlotBuilder addSlot(int slotIndex, RecipeIngredientRole recipeIngredientRole, int x, int y);

	IIngredientAcceptor<?> addInvisibleIngredients(RecipeIngredientRole recipeIngredientRole);

	/**
	 * Moves the recipe transfer button's position relative to the recipe layout.
	 * By default the recipe transfer button is at the bottom, to the right of the recipe.
	 * If it doesn't fit there, you can use this to move it when you init the recipe layout.
	 */
	void moveRecipeTransferButton(int posX, int posY);

	/**
	 * Adds a shapeless icon to the top right of the recipe, that shows a tooltip saying "shapeless" when hovered over.
	 */
	void setShapeless();
}
