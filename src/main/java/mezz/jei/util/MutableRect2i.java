package mezz.jei.util;

import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.Rect2i;

import javax.annotation.Nonnegative;

@SuppressWarnings("ConstantConditions")
public class MutableRect2i {
	@Nonnegative
	private int x;
	@Nonnegative
	private int y;
	@Nonnegative
	private int width;
	@Nonnegative
	private int height;

	public MutableRect2i(ImmutableRect2i rect) {
		this.x = rect.getX();
		this.y = rect.getY();
		this.width = rect.getWidth();
		this.height = rect.getHeight();
	}

	public MutableRect2i(Rect2i rect) {
		this(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	public MutableRect2i(@Nonnegative int x, @Nonnegative int y, @Nonnegative int width, @Nonnegative int height) {
		Preconditions.checkArgument(x >= 0, "x must be greater or equal 0");
		Preconditions.checkArgument(y >= 0, "y must be greater or equal 0");
		Preconditions.checkArgument(width >= 0, "width must be greater or equal 0");
		Preconditions.checkArgument(height >= 0, "height must be greater or equal 0");
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public MutableRect2i matchWidthAndX(ImmutableRect2i rect) {
		this.x = rect.getX();
		this.width = rect.getWidth();
		return this;
	}

	public MutableRect2i matchHeightAndY(ImmutableRect2i rect) {
		this.y = rect.getY();
		this.height = rect.getHeight();
		return this;
	}

	public MutableRect2i addOffset(@Nonnegative int x, @Nonnegative int y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public MutableRect2i moveRight(@Nonnegative int x) {
		this.x += x;
		return this;
	}

	public MutableRect2i moveLeft(@Nonnegative int x) {
		this.x -= x;
		return this;
	}

	public MutableRect2i moveDown(@Nonnegative int y) {
		this.y += y;
		return this;
	}

	public MutableRect2i moveUp(@Nonnegative int y) {
		this.y -= y;
		return this;
	}

	public MutableRect2i insetByPadding(@Nonnegative int padding) {
		Preconditions.checkArgument(padding * 2 <= this.height, "padding * 2 must not be greater than the height");
		Preconditions.checkArgument(padding * 2 <= this.width, "padding * 2 must not be greater than the width");

		this.x += padding;
		this.y += padding;
		this.width -= padding * 2;
		this.height -= padding * 2;
		return this;
	}

	public MutableRect2i expandByPadding(@Nonnegative int padding) {
		this.x -= padding;
		this.y -= padding;
		this.width += padding * 2;
		this.height += padding * 2;
		return this;
	}

	public MutableRect2i cropRight(@Nonnegative int amount) {
		Preconditions.checkArgument(amount >= 0, "amount must be greater or equal to than 0");
		Preconditions.checkArgument(amount <= this.width, "amount must not be greater than the width");

		this.width -= amount;
		return this;
	}

	public MutableRect2i cropLeft(@Nonnegative int amount) {
		Preconditions.checkArgument(amount >= 0, "amount must be greater or equal to than 0");
		Preconditions.checkArgument(amount <= this.width, "amount must not be greater than the width");

		this.x += amount;
		this.width -= amount;
		return this;
	}

	public MutableRect2i cropBottom(@Nonnegative int amount) {
		Preconditions.checkArgument(amount >= 0, "amount must be greater or equal to than 0");
		Preconditions.checkArgument(amount <= this.height, "amount must not be greater than the height");

		this.height -= amount;
		return this;
	}

	public MutableRect2i cropTop(@Nonnegative int amount) {
		Preconditions.checkArgument(amount >= 0, "amount must be greater or equal to than 0");
		Preconditions.checkArgument(amount <= this.height, "amount must not be greater than the height");

		this.y += amount;
		this.height -= amount;
		return this;
	}

	public MutableRect2i keepTop(@Nonnegative int amount) {
		Preconditions.checkArgument(amount >= 0, "amount must be greater or equal to than 0");

		this.height = amount;
		return this;
	}

	public MutableRect2i keepBottom(@Nonnegative int amount) {
		Preconditions.checkArgument(amount >= 0, "amount must be greater or equal to than 0");

		int cropAmount = this.height - amount;
		return cropTop(cropAmount);
	}

	public MutableRect2i keepRight(@Nonnegative int amount) {
		Preconditions.checkArgument(amount >= 0, "amount must be greater or equal to than 0");

		int cropAmount = this.width - amount;
		return cropLeft(cropAmount);
	}

	public MutableRect2i keepLeft(@Nonnegative int amount) {
		Preconditions.checkArgument(amount >= 0, "amount must be greater or equal to than 0");

		this.width = amount;
		return this;
	}

	public ImmutableRect2i toImmutable() {
		return new ImmutableRect2i(x, y, width, height);
	}
}
