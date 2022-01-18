package mezz.jei.gui.overlay.bookmarks;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.runtime.IBookmarkOverlay;
import mezz.jei.bookmarks.BookmarkList;
import mezz.jei.config.IClientConfig;
import mezz.jei.config.IWorldConfig;
import mezz.jei.gui.elements.GuiIconToggleButton;
import mezz.jei.gui.overlay.IngredientGrid;
import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import mezz.jei.gui.textures.Textures;
import mezz.jei.input.IClickedIngredient;
import mezz.jei.input.IRecipeFocusSource;
import mezz.jei.input.mouse.IUserInputHandler;
import mezz.jei.input.mouse.handlers.CheatInputHandler;
import mezz.jei.input.mouse.handlers.CombinedInputHandler;
import mezz.jei.input.mouse.handlers.ProxyInputHandler;
import mezz.jei.util.ImmutableRect2i;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

public class BookmarkOverlay implements IRecipeFocusSource, ILeftAreaContent, IBookmarkOverlay {
	private static final int BUTTON_SIZE = 20;
	private static final int MIN_NAVIGATION_WIDTH = 4 * BUTTON_SIZE;

	// areas
	private ImmutableRect2i parentArea = ImmutableRect2i.EMPTY;

	// display elements
	private final IngredientGridWithNavigation contents;
	private final GuiIconToggleButton bookmarkButton;

	// visibility
	private boolean hasRoom = false;

	// data
	private final BookmarkList bookmarkList;
	private final IClientConfig clientConfig;
	private final IWorldConfig worldConfig;

	public BookmarkOverlay(BookmarkList bookmarkList, Textures textures, IngredientGridWithNavigation contents, IClientConfig clientConfig, IWorldConfig worldConfig) {
		this.bookmarkList = bookmarkList;
		this.clientConfig = clientConfig;
		this.worldConfig = worldConfig;
		this.bookmarkButton = BookmarkButton.create(this, bookmarkList, textures, worldConfig);
		this.contents = contents;
		bookmarkList.addListener(() -> {
			worldConfig.setBookmarkEnabled(!bookmarkList.isEmpty());
			contents.updateLayout(false);
		});
	}

	public boolean isListDisplayed() {
		return worldConfig.isBookmarkOverlayEnabled() && hasRoom && !bookmarkList.isEmpty();
	}

	public boolean hasRoom() {
		return hasRoom;
	}

	@Override
	public void updateBounds(ImmutableRect2i area, Set<ImmutableRect2i> guiExclusionAreas) {
		this.parentArea = area;
		hasRoom = updateBounds(guiExclusionAreas);
	}

	@Override
	public void drawScreen(Minecraft minecraft, PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		if (this.isListDisplayed()) {
			this.contents.draw(minecraft, poseStack, mouseX, mouseY, partialTicks);
		}
		this.bookmarkButton.draw(poseStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void drawTooltips(Minecraft minecraft, PoseStack poseStack, int mouseX, int mouseY) {
		if (isListDisplayed()) {
			this.contents.drawTooltips(minecraft, poseStack, mouseX, mouseY);
		}
		bookmarkButton.drawTooltips(poseStack, mouseX, mouseY);
	}

	private static int getMinWidth(IClientConfig clientConfig) {
		int minIngredientsWidth = clientConfig.getMinColumns() * IngredientGrid.INGREDIENT_WIDTH;
		return Math.max(MIN_NAVIGATION_WIDTH, minIngredientsWidth);
	}

	public boolean updateBounds(Set<ImmutableRect2i> guiExclusionAreas) {
		final int minWidth = getMinWidth(this.clientConfig);
		if (parentArea.getWidth() < minWidth) {
			return false;
		}

		ImmutableRect2i availableContentsArea = parentArea.toMutable()
			.cropBottom(BUTTON_SIZE)
			.toImmutable();
		boolean contentsHasRoom = this.contents.updateBounds(availableContentsArea, guiExclusionAreas);

		ImmutableRect2i contentsArea = this.contents.getBackgroundArea();

		ImmutableRect2i bookmarkButtonArea = parentArea.toMutable()
			.matchWidthAndX(contentsArea)
			.keepBottom(BUTTON_SIZE)
			.keepLeft(BUTTON_SIZE)
			.toImmutable();
		this.bookmarkButton.updateBounds(bookmarkButtonArea);

		this.contents.updateLayout(false);

		return contentsHasRoom;
	}

	@Override
	public Optional<IClickedIngredient<?>> getIngredientUnderMouse(double mouseX, double mouseY) {
		if (isListDisplayed()) {
			return this.contents.getIngredientUnderMouse(mouseX, mouseY);
		}
		return Optional.empty();
	}

	@Nullable
	@Override
	public <T> T getIngredientUnderMouse(IIngredientType<T> ingredientType) {
		if (isListDisplayed()) {
			return this.contents.getIngredientUnderMouse(ingredientType)
				.orElse(null);
		}
		return null;
	}

	@Override
	public IUserInputHandler createInputHandler() {
		final IUserInputHandler bookmarkButtonInputHandler = this.bookmarkButton.createInputHandler();

		final IUserInputHandler displayedInputHandler = new CombinedInputHandler(
			new CheatInputHandler(this, worldConfig, clientConfig),
			this.contents.createInputHandler(),
			bookmarkButtonInputHandler
		);

		return new ProxyInputHandler(() -> {
			if (isListDisplayed()) {
				return displayedInputHandler;
			}
			return bookmarkButtonInputHandler;
		});
	}
}
