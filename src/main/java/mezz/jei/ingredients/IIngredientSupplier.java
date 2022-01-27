package mezz.jei.ingredients;

import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeIngredientRole;

import java.util.List;
import java.util.stream.Stream;

public interface IIngredientSupplier {
	List<? extends IIngredientType<?>> getIngredientTypes(RecipeIngredientRole role);
	<T> Stream<T> getIngredients(IIngredientType<T> ingredientType, RecipeIngredientRole role);
}
