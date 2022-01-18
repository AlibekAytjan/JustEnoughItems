package mezz.jei.input;

import mezz.jei.util.ImmutableRect2i;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IClickedIngredient<V> {

	V getValue();

	@Nullable
	ImmutableRect2i getArea();

	ItemStack getCheatItemStack();

	/**
	 * Some GUIs (like vanilla) shouldn't allow JEI to click to set the focus,
	 * it would conflict with their normal behavior.
	 */
	boolean canSetFocusWithMouse();
}
