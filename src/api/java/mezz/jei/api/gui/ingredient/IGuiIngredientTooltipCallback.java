package mezz.jei.api.gui.ingredient;

import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Used to add tooltips to ingredients drawn on a recipe.
 * Implement a tooltip callback and set it with {@link IGuiIngredientGroup#addTooltipCallback(IGuiIngredientTooltipCallback)}.
 * Note that this works on anything that implements {@link IGuiIngredientGroup}, like {@link IGuiItemStackGroup} and {@link IGuiFluidStackGroup}.
 * @since JEI 9.3.0
 */
@FunctionalInterface
public interface IGuiIngredientTooltipCallback<T> {
	/**
	 * Change the tooltip for an ingredient.
	 * @since JEI 9.3.0
	 */
	void onTooltip(IGuiIngredient<T> guiIngredient, List<Component> tooltip);
}
