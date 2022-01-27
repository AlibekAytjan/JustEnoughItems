package mezz.jei.gui.recipes;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeLayoutSlotBuilder;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.gui.Focus;
import mezz.jei.ingredients.IIngredientSupplier;
import mezz.jei.ingredients.IngredientsForTypeMap;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RecipeLayoutBuilder implements IRecipeLayoutBuilder, IIngredientSupplier {
	private final Int2ObjectMap<RecipeLayoutSlotBuilder> slots = new Int2ObjectArrayMap<>();
	private final Map<RecipeIngredientRole, IngredientsForTypeMap> invisibleIngredients = new EnumMap<>(RecipeIngredientRole.class);
	private boolean used = false; // for compatibility, detect if this builder was used at all
	private boolean shapeless = false;
	private int recipeTransferX = -1;
	private int recipeTransferY = -1;

	@Override
	public IRecipeLayoutSlotBuilder addSlot(int slotIndex, RecipeIngredientRole role, int x, int y) {
		this.used = true;
		if (this.slots.containsKey(slotIndex)) {
			throw new IllegalArgumentException("A slot has already been created at slot index " + slotIndex);
		}
		RecipeLayoutSlotBuilder slotBuilder = new RecipeLayoutSlotBuilder(slotIndex, role, x, y);
		this.slots.put(slotIndex, slotBuilder);
		return slotBuilder;
	}

	@Override
	public IIngredientAcceptor<?> addInvisibleIngredients(RecipeIngredientRole role) {
		this.used = true;
		return this.invisibleIngredients.computeIfAbsent(role, (key) -> new IngredientsForTypeMap());
	}

	@Override
	public void moveRecipeTransferButton(int posX, int posY) {
		this.used = true;
		this.recipeTransferX = posX;
		this.recipeTransferY = posY;
	}

	@Override
	public void setShapeless() {
		this.used = true;
		this.shapeless = true;
	}

	public boolean isUsed() {
		return used;
	}

	public <R> RecipeLayout<R> buildRecipeLayout(int index, IRecipeCategory<R> recipeCategory, R recipe, List<Focus<?>> focuses, int posX, int posY) {
		RecipeLayout<R> recipeLayout = new RecipeLayout<>(index, recipeCategory, recipe, focuses, posX, posY);
		if (this.shapeless) {
			recipeLayout.setShapeless();
		}
		if (this.recipeTransferX >= 0 && this.recipeTransferY >= 0) {
			recipeLayout.moveRecipeTransferButton(this.recipeTransferX, this.recipeTransferY);
		}

		for (RecipeLayoutSlotBuilder slot : this.slots.values()) {
			slot.setRecipeLayout(recipeLayout);
		}
		return recipeLayout;
	}

	@Override
	public List<? extends IIngredientType<?>> getIngredientTypes(RecipeIngredientRole role) {
		return this.slots.values().stream()
			.flatMap(slot -> slot.getIngredientTypes(role).stream())
			.distinct()
			.toList();
	}

	@Override
	public <T> Stream<T> getIngredients(IIngredientType<T> ingredientType, RecipeIngredientRole role) {
		return this.slots.values().stream()
			.flatMap(slot -> slot.getIngredients(ingredientType, role));
	}
}
