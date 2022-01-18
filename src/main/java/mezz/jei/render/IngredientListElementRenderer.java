package mezz.jei.render;

import mezz.jei.util.ImmutableRect2i;

public class IngredientListElementRenderer<T> {
	private static final ImmutableRect2i DEFAULT_AREA = new ImmutableRect2i(0, 0, 16, 16);

	private final T ingredient;
	private ImmutableRect2i area = DEFAULT_AREA;
	private int padding;

	public IngredientListElementRenderer(T ingredient) {
		this.ingredient = ingredient;
	}

	public void setArea(ImmutableRect2i area) {
		this.area = area;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public T getIngredient() {
		return ingredient;
	}

	public ImmutableRect2i getArea() {
		return area;
	}

	public int getPadding() {
		return padding;
	}
}
