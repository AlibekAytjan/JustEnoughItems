package mezz.jei.gui.recipes;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.gui.ingredients.GuiIngredientGroup;
import mezz.jei.ingredients.IIngredientSupplier;
import mezz.jei.ingredients.IngredientsForTypeMap;
import mezz.jei.plugins.vanilla.ingredients.fluid.FluidStackRenderer;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RecipeLayoutSlotBuilder implements IRecipeLayoutSlotBuilder, IIngredientSupplier {
	private final int slotIndex;
	private final RecipeIngredientRole role;
	private final int xPos;
	private final int yPos;
	@Nullable
	private IDrawable background;
	private final IngredientsForTypeMap ingredients = new IngredientsForTypeMap();
	private final Map<IIngredientType<?>, RecipeLayoutSlotRenderOverride<?>> renderOverrides = new IdentityHashMap<>();

	public RecipeLayoutSlotBuilder(int slotIndex, RecipeIngredientRole role, int x, int y) {
		this.slotIndex = slotIndex;
		this.role = role;
		this.xPos = x;
		this.yPos = y;
	}

	@Override
	public <I> IRecipeLayoutSlotBuilder addIngredients(IIngredientType<I> ingredientType, List<I> ingredients) {
		this.ingredients.addIngredients(ingredientType, ingredients);
		return this;
	}

	@Override
	public <I> IRecipeLayoutSlotBuilder addIngredient(IIngredientType<I> ingredientType, I ingredient) {
		this.ingredients.addIngredient(ingredientType, ingredient);
		return this;
	}

	@Override
	public IRecipeLayoutSlotBuilder setBackground(IDrawable background) {
		this.background = background;
		return this;
	}

	@Override
	public IRecipeLayoutSlotBuilder setFluidRenderer(int width, int height, int xPadding, int yPadding, int capacityMb, boolean showCapacity, @Nullable IDrawable overlay) {
		FluidStackRenderer fluidStackRenderer = new FluidStackRenderer(capacityMb, showCapacity, width, height, overlay);
		return setCustomRenderer(VanillaTypes.FLUID, fluidStackRenderer, width, height, xPadding, yPadding);
	}

	@Override
	public <T> IRecipeLayoutSlotBuilder setCustomRenderer(IIngredientType<T> ingredientType, IIngredientRenderer<T> ingredientRenderer, int width, int height, int xPadding, int yPadding) {
		RecipeLayoutSlotRenderOverride<T> override = new RecipeLayoutSlotRenderOverride<>(ingredientRenderer, width, height, xPadding, yPadding);
		this.renderOverrides.put(ingredientType, override);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public <T> RecipeLayoutSlotRenderOverride<T> getRenderOverride(IIngredientType<T> ingredientType) {
		return (RecipeLayoutSlotRenderOverride<T>) this.renderOverrides.get(ingredientType);
	}

	public <R> void setRecipeLayout(RecipeLayout<R> recipeLayout) {
		IngredientsForTypeMap ingredients = this.ingredients;
		for (IIngredientType<?> type : ingredients.getIngredientTypes()) {
			setRecipeLayout(type, recipeLayout);
		}
	}

	private <T, R> void setRecipeLayout(IIngredientType<T> ingredientType, RecipeLayout<R> recipeLayout) {
		GuiIngredientGroup<T> ingredientsGroup = recipeLayout.getIngredientsGroup(ingredientType);

		RecipeLayoutSlotRenderOverride<T> renderOverride = this.getRenderOverride(ingredientType);
		if (renderOverride != null) {
			IIngredientRenderer<T> ingredientRenderer = renderOverride.getIngredientRenderer();
			ingredientsGroup.init(
				this.slotIndex,
				this.role,
				ingredientRenderer,
				this.xPos,
				this.yPos,
				renderOverride.getWidth(),
				renderOverride.getHeight(),
				renderOverride.getXPadding(),
				renderOverride.getYPadding()
			);
		} else {
			ingredientsGroup.init(
				this.slotIndex,
				this.role,
				this.xPos,
				this.yPos
			);
		}

		List<T> ingredients = this.ingredients
			.getIngredients(ingredientType)
			.toList();
		ingredientsGroup.set(this.slotIndex, ingredients);

		if (this.background != null) {
			ingredientsGroup.setBackground(this.slotIndex, this.background);
		}
	}

	@Override
	public List<? extends IIngredientType<?>> getIngredientTypes(RecipeIngredientRole role) {
		if (this.role == role) {
			return this.ingredients.getIngredientTypes();
		}
		return List.of();
	}

	@Override
	public <T> Stream<T> getIngredients(IIngredientType<T> ingredientType, RecipeIngredientRole role) {
		if (this.role == role) {
			return this.ingredients.getIngredients(ingredientType);
		}
		return Stream.empty();
	}
}
