package mezz.jei.util;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.Rect2i;

import javax.annotation.Nonnegative;

public class ImmutableRect2i {
	public static final ImmutableRect2i EMPTY = new ImmutableRect2i(0, 0, 0, 0);

	@Nonnegative
	private final int x;
	@Nonnegative
	private final int y;
	@Nonnegative
	private final int width;
	@Nonnegative
	private final int height;

	public ImmutableRect2i(Rect2i rect) {
		this(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	@SuppressWarnings("ConstantConditions")
	public ImmutableRect2i(@Nonnegative int x, @Nonnegative int y, @Nonnegative int width, @Nonnegative int height) {
		Preconditions.checkArgument(x >= 0, "x must be greater or equal 0");
		Preconditions.checkArgument(y >= 0, "y must be greater or equal 0");
		Preconditions.checkArgument(width >= 0, "width must be greater or equal 0");
		Preconditions.checkArgument(height >= 0, "height must be greater or equal 0");
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Nonnegative
	public int getX() {
		return x;
	}

	@Nonnegative
	public int getY() {
		return y;
	}

	@Nonnegative
	public int getWidth() {
		return width;
	}

	@Nonnegative
	public int getHeight() {
		return height;
	}

	public MutableRect2i toMutable() {
		return new MutableRect2i(this);
	}

	@Override
	public boolean equals(Object obj){
		if (obj instanceof ImmutableRect2i other) {
			return
				x == other.x &&
				y == other.y &&
				width == other.width &&
				height == other.height;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + x;
		hash = hash * 31 + y;
		hash = hash * 31 + width;
		hash = hash * 31 + height;
		return hash;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("x", x)
			.add("y", y)
			.add("width", width)
			.add("height", height)
			.toString();
	}
}
