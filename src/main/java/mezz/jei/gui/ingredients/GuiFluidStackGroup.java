package mezz.jei.gui.ingredients;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.FluidStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.plugins.vanilla.ingredients.fluid.FluidStackRenderer;

public class GuiFluidStackGroup extends GuiIngredientGroup<FluidStack> implements IGuiFluidStackGroup {
	public GuiFluidStackGroup(int cycleOffset) {
		super(VanillaTypes.FLUID, cycleOffset);
	}

	@Override
	public void init(int slotIndex, boolean input, int xPosition, int yPosition, int width, int height, int capacityMb, boolean showCapacity, @Nullable IDrawable overlay) {
		FluidStackRenderer renderer = new FluidStackRenderer(capacityMb, showCapacity, width, height, overlay);
		init(slotIndex, input, renderer, xPosition, yPosition, width, height, 0, 0);
	}
}
