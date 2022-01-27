package mezz.jei.api.gui.builder;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface IIngredientAcceptor<THIS extends IIngredientAcceptor<THIS>> {
	<I> THIS addIngredients(IIngredientType<I> ingredientType, List<I> ingredients);

	<I> THIS addIngredient(IIngredientType<I> ingredientType, I ingredient);

	default THIS addIngredients(List<ItemStack> itemStacks) {
		return addIngredients(VanillaTypes.ITEM, itemStacks);
	}

	default THIS addIngredients(Ingredient ingredient) {
		return addIngredients(VanillaTypes.ITEM, List.of(ingredient.getItems()));
	}

	default THIS addIngredient(ItemStack itemStack) {
		return addIngredient(VanillaTypes.ITEM, itemStack);
	}
}
