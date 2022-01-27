package mezz.jei.api.gui.builder;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;

import javax.annotation.Nullable;

public interface IRecipeLayoutSlotBuilder extends IIngredientAcceptor<IRecipeLayoutSlotBuilder> {
	IRecipeLayoutSlotBuilder setBackground(IDrawable background);
	<T> IRecipeLayoutSlotBuilder setFluidRenderer(int width, int height, int xPadding, int yPadding, int capacityMb, boolean showCapacity, @Nullable IDrawable overlay);
	<T> IRecipeLayoutSlotBuilder setCustomRenderer(IIngredientType<T> ingredientType, IIngredientRenderer<T> ingredientRenderer, int width, int height, int xPadding, int yPadding);

	// TODO: recipe layout slot tooltip callback
//	void addTooltipCallback(IGuiIngredientTooltipCallback<T> tooltipCallback);
}
