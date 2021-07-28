package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.Vector2D;
import com.github.glassmc.sculpt.framework.constraint.*;
import com.github.glassmc.sculpt.framework.layout.Layout;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;
import com.github.glassmc.sculpt.framework.util.Axis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Container extends Element {

    private Layout layout = new RegionLayout();

    private Constraint x = new Flexible(), y = new Flexible();

    private boolean backgroundEnabled = false;
    private Constraint backgroundColor = new Absolute(new Color(1., 1., 1.));

    private Constraint width = new Flexible(), height = new Flexible();
    private final Map<Direction, Constraint> padding = new HashMap<Direction, Constraint>() {
        {
            for(Direction direction : Direction.values()) {
                put(direction, new Flexible());
            }
        }
    };

    private Constraint cornerRadius = new Flexible();

    private final List<Element> children = new ArrayList<>();

    private Consumer<Container> onClick;

    public Container() {
        this.possibleConstructors.add(new Constructor<>());
        this.layout.setContainer(this);
        this.x.setElement(this);
        this.y.setElement(this);
        this.width.setElement(this);
        this.height.setElement(this);

        for(Constraint padding : this.padding.values()) {
            padding.setElement(this);
        }
    }

    @SuppressWarnings("unused")
    public Container x(Constraint x) {
        this.x = x;
        x.setElement(this);
        return this;
    }

    public Constraint getX() {
        return x;
    }

    @SuppressWarnings("unused")
    public Container y(Constraint y) {
        this.y = y;
        y.setElement(this);
        return this;
    }

    public Constraint getY() {
        return y;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Container add(Element element) {
        this.children.add(element);
        element.setParent(this);
        return this;
    }

    public List<Element> getChildren() {
        return this.children;
    }

    @SuppressWarnings("unused")
    public Container width(Constraint width) {
        this.width = width;
        width.setElement(this);
        return this;
    }

    public Constraint getWidth() {
        return width;
    }

    @SuppressWarnings("unused")
    public Container height(Constraint height) {
        this.height = height;
        height.setElement(this);
        return this;
    }

    public Constraint getHeight() {
        return height;
    }

    public Container cornerRadius(Constraint cornerRadius) {
        this.cornerRadius = cornerRadius;
        cornerRadius.setElement(this);
        return this;
    }

    public Constraint getCornerRadius() {
        return cornerRadius;
    }

    @SuppressWarnings("unused")
    public Container backgroundEnabled(boolean backgroundEnabled) {
        this.backgroundEnabled = backgroundEnabled;
        return this;
    }

    public boolean isBackgroundEnabled() {
        return backgroundEnabled;
    }

    @SuppressWarnings("unused")
    public Container backgroundColor(Constraint backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundColor.setElement(this);
        this.backgroundEnabled = true;
        return this;
    }

    public Constraint getBackgroundColor() {
        return backgroundColor;
    }

    @SuppressWarnings("unused")
    public Container padding(Constraint padding) {
        for(Direction direction : Direction.values()) {
            this.padding.put(direction, padding);
        }
        padding.setElement(this);
        return this;
    }

    @SuppressWarnings("unused")
    public Container padding(Direction direction, Constraint padding) {
        this.padding.put(direction, padding);
        padding.setElement(this);
        return this;
    }

    public Constraint getPadding(Direction direction) {
        return padding.get(direction);
    }

    @SuppressWarnings("unused")
    public Container layout(Layout layout) {
        this.layout = layout;
        layout.setContainer(this);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Layout> T getLayout() {
        return (T) this.layout;
    }

    @SuppressWarnings("unused")
    public <T extends Layout> T getLayout(Class<T> layoutClass) {
        return layoutClass.cast(this.layout);
    }

    public Container onClick(Consumer<Container> onClick) {
        this.onClick = onClick;
        return this;
    }

    public Consumer<Container> getOnClick() {
        return onClick;
    }

    public Container apply(Consumer<Container> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public Constructor<? extends Container> getConstructor() {
        return (Constructor<? extends Container>) super.getConstructor();
    }

    public static class Constructor<T extends Container> extends Element.Constructor<T> {

        private Color backgroundColor;
        private double cornerRadius;

        @Override
        public void render(Renderer renderer, List<Element.Constructor<?>> parentAppliedElements) {
            Container container = this.getComponent();
            double width = this.getWidth();
            double height = this.getHeight();

            this.cornerRadius = this.getComponent().getCornerRadius().getConstructor().getCornerRadiusValue(renderer, parentAppliedElements);

            this.backgroundColor = this.getComponent().getBackgroundColor().getConstructor().getColorValue(renderer, parentAppliedElements);

            for(Vector2D vector2D : renderer.getBackend().getMouseClicks()) {
                if(this.isOnTop(vector2D, this)) {
                    if(container.getOnClick() != null) {
                        container.getOnClick().accept(this.getComponent());
                    }
                }
            }

            if(container.isBackgroundEnabled()) {
                Color color = container.getBackgroundColor().getConstructor().getColorValue(renderer, parentAppliedElements);
                renderer.getBackend().drawRectangle(this.getCalculatedX(), this.getCalculatedY(), width, height, this.cornerRadius, color);
            }

            Layout layout = container.getLayout();

            List<Element.Constructor<?>> layoutElementData = new ArrayList<>(layout.getConstructor().getDefaultElements());

            List<Element.Constructor<?>> appliedElements = new ArrayList<>();
            appliedElements.add(this.createPseudoConstructor(-width, 0, width, height * 16));
            appliedElements.add(this.createPseudoConstructor(width, 0, width, height * 16));
            appliedElements.add(this.createPseudoConstructor(0, -height, width * 16, height));
            appliedElements.add(this.createPseudoConstructor(0, height, width * 16, height));

            for(Element.Constructor<?> element : layoutElementData) {
                this.computeChildPaddings(element);
                for(int i = 0; i < 3; i++) {
                    this.adjustElementPosition(element, appliedElements);
                    this.applyElementSizeRequests(renderer, element, appliedElements);
                }
                this.applyElementPositionRequests(element, appliedElements);

                appliedElements.add(element);

                element.render(renderer, appliedElements);
            }
        }

        protected Element.Constructor<?> createPseudoConstructor(double x, double y, double width, double height) {
            Element.Constructor<?> constructor = new Element.Constructor<>();
            constructor.setX(x);
            constructor.setY(y);
            constructor.setWidth(width);
            constructor.setHeight(height);
            return constructor;
        }

        protected void computeChildPaddings(Element.Constructor<?> element) {
            element.computePaddings();
        }

        @Override
        protected void computePaddings() {
            for(Direction direction : Direction.values()) {
                this.computePaddings(direction, this.getComponent().getPadding(direction));
            }
        }

        @Override
        public double getWidth(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getComponent().getWidth().getConstructor().getWidthValue(renderer, appliedElements);
        }

        @Override
        public double getHeight(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getComponent().getHeight().getConstructor().getHeightValue(renderer, appliedElements);
        }

        @Override
        public Constraint getXConstraint() {
            return this.getComponent().getX();
        }

        @Override
        public Constraint getYConstraint() {
            return this.getComponent().getY();
        }

        protected void adjustElementPosition(Element.Constructor<?> element, List<Element.Constructor<?>> appliedElements) {
            for(Element.Constructor<?> appliedElement : appliedElements) {
                if(this.intersect(element, appliedElement, Axis.UNSPECIFIED)) {
                    Map<Double, Axis> possibleChanges = new HashMap<>();

                    MaxPaddingComputer maxPadding = new MaxPaddingComputer(element, appliedElement);

                    possibleChanges.put((appliedElement.getRight() + maxPadding.getLeft() + element.getWidth() / 2) - element.getX(), Axis.X);
                    possibleChanges.put((appliedElement.getLeft() - maxPadding.getRight() - element.getWidth() / 2) - element.getX(), Axis.X);

                    possibleChanges.put((appliedElement.getBottom() + maxPadding.getTop() + element.getHeight() / 2) - element.getY(), Axis.Y);
                    possibleChanges.put((appliedElement.getTop() - maxPadding.getBottom() - element.getHeight() / 2) - element.getY(), Axis.Y);

                    Map.Entry<Double, Axis> minChange = null;
                    for(Map.Entry<Double, Axis> change : possibleChanges.entrySet()) {
                        if(Math.abs(change.getKey()) < Math.abs(minChange == null ? Double.MAX_VALUE : minChange.getKey())) {
                            minChange = change;
                        }
                    }

                    if(minChange == null) {
                        return;
                    }

                    if(minChange.getValue() == Axis.X) {
                        element.setX(element.getX() + minChange.getKey());
                    } else if(minChange.getValue() == Axis.Y) {
                        element.setY(element.getY() + minChange.getKey());
                    }
                }
            }
        }

        protected void applyElementSizeRequests(Renderer renderer, Element.Constructor<?> element, List<Element.Constructor<?>> appliedElements) {
            this.applyElementWidthRequest(renderer, element, appliedElements);
            this.applyElementHeightRequest(renderer, element, appliedElements);
        }

        private void applyElementWidthRequest(Renderer renderer, Element.Constructor<?> element, List<Element.Constructor<?>> appliedElements) {
            element.setWidth(element.getWidth(renderer, appliedElements));
        }

        private void applyElementHeightRequest(Renderer renderer, Element.Constructor<?> element, List<Element.Constructor<?>> appliedElements) {
            element.setHeight(element.getHeight(renderer, appliedElements));
        }

        protected void applyElementPositionRequests(Element.Constructor<?> element, List<Element.Constructor<?>> appliedElements) {
            this.applyElementXRequest(element, appliedElements);
            this.applyElementYRequest(element, appliedElements);
        }

        protected void applyElementXRequest(Element.Constructor<?> element, List<Element.Constructor<?>> appliedElements) {
            element.setX(element.getXConstraint().getConstructor().getXValue(appliedElements));
        }

        protected void applyElementYRequest(Element.Constructor<?> element, List<Element.Constructor<?>> appliedElements) {
            element.setY(element.getYConstraint().getConstructor().getYValue(appliedElements));
        }

        public Color getBackgroundColor() {
            return backgroundColor;
        }

    }

}
