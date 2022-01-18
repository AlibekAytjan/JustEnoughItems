package mezz.jei.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.input.mouse.handlers.CombinedInputHandler;
import mezz.jei.input.mouse.IUserInputHandler;
import mezz.jei.util.ImmutableRect2i;
import mezz.jei.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;

import mezz.jei.Internal;
import mezz.jei.gui.elements.GuiIconButton;
import mezz.jei.gui.textures.Textures;
import mezz.jei.input.IPaged;

public class PageNavigation {
	private final IPaged paged;
	private final GuiIconButton nextButton;
	private final GuiIconButton backButton;
	private final boolean hideOnSinglePage;
	private String pageNumDisplayString = "1/1";
	private int pageNumDisplayX;
	private int pageNumDisplayY;
	private ImmutableRect2i area = ImmutableRect2i.EMPTY;

	public PageNavigation(IPaged paged, boolean hideOnSinglePage) {
		this.paged = paged;
		Textures textures = Internal.getTextures();
		this.nextButton = new GuiIconButton(textures.getArrowNext(), b -> paged.nextPage());
		this.backButton = new GuiIconButton(textures.getArrowPrevious(), b -> paged.previousPage());
		this.hideOnSinglePage = hideOnSinglePage;
	}

	public void updateBounds(ImmutableRect2i area) {
		this.area = area;
		int buttonSize = area.getHeight();

		ImmutableRect2i backArea = area.toMutable()
			.keepLeft(buttonSize)
			.toImmutable();
		this.backButton.updateBounds(backArea);

		ImmutableRect2i nextArea = area.toMutable()
			.keepRight(buttonSize)
			.toImmutable();
		this.nextButton.updateBounds(nextArea);
	}

	public void updatePageState() {
		int pageNum = this.paged.getPageNumber();
		int pageCount = this.paged.getPageCount();
		Minecraft minecraft = Minecraft.getInstance();
		Font fontRenderer = minecraft.font;
		this.pageNumDisplayString = (pageNum + 1) + "/" + pageCount;
		ImmutableRect2i centerArea = MathUtil.centerTextArea(this.area, fontRenderer, this.pageNumDisplayString);
		this.pageNumDisplayX = centerArea.getX();
		this.pageNumDisplayY = centerArea.getY();
	}

	public void draw(Minecraft minecraft, PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		if (!hideOnSinglePage || this.paged.hasNext() || this.paged.hasPrevious()) {
			GuiComponent.fill(poseStack,
				backButton.x + backButton.getWidth(),
				backButton.y,
				nextButton.x,
				nextButton.y + nextButton.getHeight(),
				0x30000000);

			minecraft.font.drawShadow(poseStack, pageNumDisplayString, pageNumDisplayX, pageNumDisplayY, 0xFFFFFFFF);
			nextButton.render(poseStack, mouseX, mouseY, partialTicks);
			backButton.render(poseStack, mouseX, mouseY, partialTicks);
		}
	}

	public IUserInputHandler createInputHandler() {
		return new CombinedInputHandler(
			this.nextButton.createInputHandler(),
			this.backButton.createInputHandler()
		);
	}
}
