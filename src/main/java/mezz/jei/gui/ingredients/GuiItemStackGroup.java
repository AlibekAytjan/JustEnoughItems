package mezz.jei.gui.ingredients;

import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.plugins.vanilla.ingredients.item.ItemStackRenderer;

public class GuiItemStackGroup extends GuiIngredientGroup<ItemStack> implements IGuiItemStackGroup {
	private static final ItemStackRenderer renderer = new ItemStackRenderer();

	public GuiItemStackGroup(int cycleOffset) {
		super(VanillaTypes.ITEM, cycleOffset);
	}

	@Override
	public void init(int slotIndex, boolean input, int xPosition, int yPosition) {
		init(slotIndex, input, renderer, xPosition, yPosition, GuiIngredientProperties.getWidth(1), GuiIngredientProperties.getHeight(1), 1, 1);
	}

	@Override
	public void init(int slotIndex, RecipeIngredientRole role, int xPosition, int yPosition) {
		init(slotIndex, role, renderer, xPosition, yPosition, GuiIngredientProperties.getWidth(1), GuiIngredientProperties.getHeight(1), 1, 1);
	}
}
