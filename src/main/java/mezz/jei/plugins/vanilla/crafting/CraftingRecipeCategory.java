package mezz.jei.plugins.vanilla.crafting;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeLayoutSlotBuilder;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.resources.ResourceLocation;

import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICustomCraftingCategoryExtension;
import mezz.jei.config.Constants;
import mezz.jei.recipes.ExtendableRecipeCategoryHelper;

import javax.annotation.Nullable;

public class CraftingRecipeCategory implements IExtendableRecipeCategory<CraftingRecipe, ICraftingCategoryExtension> {
	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 1;

	public static final int width = 116;
	public static final int height = 54;

	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;
	private final ICraftingGridHelper craftingGridHelper;
	private final ExtendableRecipeCategoryHelper<Recipe<?>, ICraftingCategoryExtension> extendableHelper = new ExtendableRecipeCategoryHelper<>(CraftingRecipe.class);

	public CraftingRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation location = Constants.RECIPE_GUI_VANILLA;
		background = guiHelper.createDrawable(location, 0, 60, width, height);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(Blocks.CRAFTING_TABLE));
		localizedName = new TranslatableComponent("gui.jei.category.craftingTable");
		craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1);
	}

	@Override
	public ResourceLocation getUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	public Class<? extends CraftingRecipe> getRecipeClass() {
		return CraftingRecipe.class;
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

//	@Override
//	public void setRecipe(IRecipeLayout recipeLayout, CraftingRecipe recipe, IIngredients ingredients) {
//		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
//
//		guiItemStacks.init(craftOutputSlot, false, 94, 18);
//
//		for (int y = 0; y < 3; ++y) {
//			for (int x = 0; x < 3; ++x) {
//				int index = craftInputSlot1 + x + (y * 3);
//				guiItemStacks.init(index, true, x * 18, y * 18);
//			}
//		}
//
//		ICraftingCategoryExtension recipeExtension = this.extendableHelper.getRecipeExtension(recipe);
//
//		if (recipeExtension instanceof ICustomCraftingCategoryExtension customExtension) {
//			customExtension.setRecipe(recipeLayout, ingredients);
//			return;
//		}
//
//		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
//		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
//
//		int width = recipeExtension.getWidth();
//		int height = recipeExtension.getHeight();
//		if (width > 0 && height > 0) {
//			craftingGridHelper.setInputs(guiItemStacks, inputs, width, height);
//		} else {
//			craftingGridHelper.setInputs(guiItemStacks, inputs);
//			recipeLayout.setShapeless();
//		}
//		guiItemStacks.set(craftOutputSlot, outputs.get(0));
//	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CraftingRecipe recipe, List<? extends IFocus<?>> focuses) {
		ICraftingCategoryExtension recipeExtension = this.extendableHelper.getRecipeExtension(recipe);

		List<IRecipeLayoutSlotBuilder> inputSlots = new ArrayList<>();
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				int index = craftInputSlot1 + x + (y * 3);
				IRecipeLayoutSlotBuilder slot = builder.addSlot(index, RecipeIngredientRole.INPUT, x * 18, y * 18);
				inputSlots.add(slot);
			}
		}

		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		int width = recipeExtension.getWidth();
		int height = recipeExtension.getHeight();
		if (width <= 0 || height <= 0) {
			builder.setShapeless();
			width = height = craftingGridHelper.getShapelessSize(ingredients.size());
		}

		for (int i = 0; i < ingredients.size(); i++) {
			int index = craftingGridHelper.getCraftingIndex(i, width, height);
			IRecipeLayoutSlotBuilder slot = inputSlots.get(index);

			Ingredient ingredient = ingredients.get(i);
			slot.addIngredients(ingredient);
		}

		builder.addSlot(craftOutputSlot, RecipeIngredientRole.OUTPUT, 94, 18)
			.addIngredient(recipe.getResultItem());
	}

//	@Override
//	public void setIngredients(CraftingRecipe recipe, IIngredients ingredients) {
//		ICraftingCategoryExtension extension = this.extendableHelper.getRecipeExtension(recipe);
//		extension.setIngredients(ingredients);
//	}

	@Override
	public void draw(CraftingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
		ICraftingCategoryExtension extension = this.extendableHelper.getRecipeExtension(recipe);
		int recipeWidth = this.background.getWidth();
		int recipeHeight = this.background.getHeight();
		extension.drawInfo(recipeWidth, recipeHeight, poseStack, mouseX, mouseY);
	}

	@Override
	public List<Component> getTooltipStrings(CraftingRecipe recipe, double mouseX, double mouseY) {
		ICraftingCategoryExtension extension = this.extendableHelper.getRecipeExtension(recipe);
		return extension.getTooltipStrings(mouseX, mouseY);
	}

	@Override
	public boolean handleInput(CraftingRecipe recipe, double mouseX, double mouseY, InputConstants.Key input) {
		ICraftingCategoryExtension extension = this.extendableHelper.getRecipeExtension(recipe);
		return extension.handleInput(mouseX, mouseY, input);
	}

	@Override
	public boolean isHandled(CraftingRecipe recipe) {
		ICraftingCategoryExtension extension = this.extendableHelper.getRecipeExtensionOrNull(recipe);
		return extension != null;
	}

	@Override
	public <R extends CraftingRecipe> void addCategoryExtension(Class<? extends R> recipeClass, Function<R, ? extends ICraftingCategoryExtension> extensionFactory) {
		extendableHelper.addRecipeExtensionFactory(recipeClass, null, extensionFactory);
	}

	@Override
	public <R extends CraftingRecipe> void addCategoryExtension(Class<? extends R> recipeClass, Predicate<R> extensionFilter, Function<R, ? extends ICraftingCategoryExtension> extensionFactory) {
		extendableHelper.addRecipeExtensionFactory(recipeClass, extensionFilter, extensionFactory);
	}
}
