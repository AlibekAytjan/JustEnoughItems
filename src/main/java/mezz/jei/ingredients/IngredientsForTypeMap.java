package mezz.jei.ingredients;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.ingredients.IIngredientType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class IngredientsForTypeMap implements IIngredientAcceptor<IngredientsForTypeMap> {
	private record IngredientListTuple<T>(IIngredientType<T> ingredientType, List<T> ingredients) {

	}

	private final List<IngredientListTuple<?>> tuples = new ArrayList<>(1);

	@Override
	public <T> IngredientsForTypeMap addIngredients(IIngredientType<T> ingredientType, List<T> ingredients) {
		IngredientListTuple<T> tuple = getOrCreateTuple(ingredientType);
		tuple.ingredients.addAll(ingredients);
		return this;
	}

	@Override
	public <T> IngredientsForTypeMap addIngredient(IIngredientType<T> ingredientType, T ingredient) {
		IngredientListTuple<T> tuple = getOrCreateTuple(ingredientType);
		tuple.ingredients.add(ingredient);
		return this;
	}

	public <T> Stream<T> getIngredients(IIngredientType<T> ingredientType) {
		IngredientListTuple<T> tuple = getTuple(ingredientType);
		if (tuple == null) {
			return Stream.empty();
		}
		return tuple.ingredients.stream();
	}

	public List<? extends IIngredientType<?>> getIngredientTypes() {
		return this.tuples.stream()
			.map(IngredientListTuple::ingredientType)
			.toList();
	}

	@Nullable
	private <T> IngredientListTuple<T> getTuple(IIngredientType<T> ingredientType) {
		for (IngredientListTuple<?> i : tuples) {
			if (i.ingredientType == ingredientType) {
				@SuppressWarnings("unchecked")
				IngredientListTuple<T> tuple = (IngredientListTuple<T>) i;
				return tuple;
			}
		}
		return null;
	}

	private <T> IngredientListTuple<T> getOrCreateTuple(IIngredientType<T> ingredientType) {
		IngredientListTuple<T> tuple = getTuple(ingredientType);
		if (tuple == null) {
			tuple = new IngredientListTuple<>(ingredientType, new ArrayList<>());
			this.tuples.add(tuple);
		}
		return tuple;
	}
}
