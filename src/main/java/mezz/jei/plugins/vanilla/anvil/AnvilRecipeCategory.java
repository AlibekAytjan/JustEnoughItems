package mezz.jei.plugins.vanilla.anvil;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.List;
import java.util.Map;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.config.Constants;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public class AnvilRecipeCategory implements IRecipeCategory<AnvilRecipe> {
	private final IDrawable background;
	private final IDrawable icon;

	public AnvilRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 0, 168, 125, 18)
			.addPadding(0, 20, 0, 0)
			.build();
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(Blocks.ANVIL));
	}

	@Override
	public ResourceLocation getUid() {
		return VanillaRecipeCategoryUid.ANVIL;
	}

	@Override
	public Class<? extends AnvilRecipe> getRecipeClass() {
		return AnvilRecipe.class;
	}

	@Override
	public Component getTitle() {
		return Blocks.ANVIL.getName();
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
//	public void setIngredients(AnvilRecipe recipe, IIngredients ingredients) {
//		ingredients.setInputLists(VanillaTypes.ITEM, List.of(recipe.getLeftInputs(), recipe.getRightInputs()));
//		ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputs());
//	}
//
//	@Override
//	public void setRecipe(IRecipeLayout recipeLayout, AnvilRecipe recipe, IIngredients ingredients) {
//		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
//
//		guiItemStacks.init(0, true, 0, 0);
//		guiItemStacks.init(1, true, 49, 0);
//		guiItemStacks.init(2, false, 107, 0);
//
//		guiItemStacks.set(ingredients);
//	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, AnvilRecipe recipe, List<? extends IFocus<?>> focuses) {
		builder.addSlot(0, RecipeIngredientRole.INPUT, 0, 0)
			.addIngredients(recipe.getLeftInputs());

		builder.addSlot(1, RecipeIngredientRole.INPUT, 49, 0)
			.addIngredients(recipe.getRightInputs());

		builder.addSlot(2, RecipeIngredientRole.OUTPUT, 107, 0)
			.addIngredients(recipe.getOutputs());

		builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
			.addIngredients(recipe.getOutputs());
	}

	@Override
	public void draw(AnvilRecipe recipe, IRecipeLayout recipeLayout, PoseStack poseStack, double mouseX, double mouseY) {
		Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients = recipeLayout.getItemStacks().getGuiIngredients();

		ItemStack leftStack = currentIngredients.get(0).getDisplayedIngredient();
		ItemStack rightStack = currentIngredients.get(1).getDisplayedIngredient();
		if (leftStack == null || rightStack == null) {
			return;
		}

		int cost = AnvilRecipeMaker.findLevelsCost(leftStack, rightStack);
		String costText = cost < 0 ? "err" : Integer.toString(cost);
		String text = I18n.get("container.repair.cost", costText);

		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		// Show red if the player doesn't have enough levels
		int mainColor = playerHasEnoughLevels(player, cost) ? 0xFF80FF20 : 0xFFFF6060;
		drawRepairCost(minecraft, poseStack, text, mainColor);
	}

	private static boolean playerHasEnoughLevels(@Nullable LocalPlayer player, int cost) {
		if (player == null) {
			return true;
		}
		if (player.isCreative()) {
			return true;
		}
		return cost < 40 && cost <= player.experienceLevel;
	}

	private void drawRepairCost(Minecraft minecraft, PoseStack poseStack, String text, int mainColor) {
		int shadowColor = 0xFF000000 | (mainColor & 0xFCFCFC) >> 2;
		int width = minecraft.font.width(text);
		int x = background.getWidth() - 2 - width;
		int y = 27;

		// TODO 1.13 match the new GuiRepair style
		minecraft.font.draw(poseStack, text, x + 1, y, shadowColor);
		minecraft.font.draw(poseStack, text, x, y + 1, shadowColor);
		minecraft.font.draw(poseStack, text, x + 1, y + 1, shadowColor);
		minecraft.font.draw(poseStack, text, x, y, mainColor);
	}
}
