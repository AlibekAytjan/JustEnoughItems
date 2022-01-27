package mezz.jei.api.recipe.category.extensions.vanilla.crafting;

import javax.annotation.Nullable;

import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraftforge.common.util.Size2i;
import net.minecraft.resources.ResourceLocation;

import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;

/**
 * Implement this interface instead of just {@link IRecipeCategoryExtension} to have your recipe extension work as part of the
 * {@link VanillaRecipeCategoryUid#CRAFTING} recipe category as a shapeless recipe.
 *
 * For shaped recipes, override {@link #getWidth()} and {@link #getHeight()}.
 * To override the category's behavior and set the recipe layout yourself, use {@link ICustomCraftingCategoryExtension}.
 *
 * Register this extension by getting the extendable crafting category from:
 * {@link IVanillaCategoryExtensionRegistration#getCraftingCategory()}
 * and then registering it with {@link IExtendableRecipeCategory#addCategoryExtension}.
 */
public interface ICraftingCategoryExtension extends IRecipeCategoryExtension {
	/**
	 * Return the registry name of the recipe here.
	 * With advanced tooltips on, this will show on the output item's tooltip.
	 *
	 * This will also show the modId when the recipe modId and output item modId do not match.
	 * This lets the player know where the recipe came from.
	 *
	 * @return the registry name of the recipe, or null if there is none
	 */
	@Nullable
	default ResourceLocation getRegistryName() {
		return null;
	}

	/**
	 * @return the width of a shaped recipe, or 0 for a shapeless recipe
	 * @since JEI 9.3.0
	 */
	default int getWidth() {
		Size2i size = getSize();
		if (size == null) {
			return 0;
		}
		return size.width;
	}

	/**
	 * @return the height of a shaped recipe, or 0 for a shapeless recipe
	 * @since JEI 9.3.0
	 */
	default int getHeight() {
		Size2i size = getSize();
		if (size == null) {
			return 0;
		}
		return size.height;
	}

	/**
	 * @return the size of a shaped recipe, or null for a shapeless recipe
	 * @deprecated since JEI 9.3.0. Use {@link #getWidth()} and {@link #getHeight()} instead.
	 */
	@Deprecated
	@Nullable
	default Size2i getSize() {
		return null;
	}
}
