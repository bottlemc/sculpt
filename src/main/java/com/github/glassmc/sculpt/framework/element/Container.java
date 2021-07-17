package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.constraint.*;
import com.github.glassmc.sculpt.framework.layout.Layout;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;
import com.github.glassmc.sculpt.framework.util.Axis;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Container extends Element {

    private Layout layout = new RegionLayout();

    private Constraint x = new Flexible(), y = new Flexible();
    private boolean backgroundEnabled = false;
    private Color backgroundColor = new Color(1., 1., 1.);
    private Constraint width = new Flexible(), height = new Flexible();
    private final Map<Direction, Constraint> padding = new HashMap<Direction, Constraint>() {
        {
            for(Direction direction : Direction.values()) {
                put(direction, new Flexible());
            }
        }
    };
    private final List<Element> children = new ArrayList<>();

    public Container() {
        this.possibleConstructors.add(new Constructor<>());
        this.layout.setContainer(this);
    }

    @SuppressWarnings("unused")
    public Container x(Constraint x) {
        this.x = x;
        return this;
    }

    @Override
    public Constraint getX() {
        return x;
    }

    @SuppressWarnings("unused")
    public Container y(Constraint y) {
        this.y = y;
        return this;
    }

    @Override
    public Constraint getY() {
        return y;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Container add(Element element) {
        this.children.add(element);
        return this;
    }

    public List<Element> getChildren() {
        return this.children;
    }

    @SuppressWarnings("unused")
    public Container width(Constraint width) {
        this.width = width;
        return this;
    }

    public Constraint getWidth() {
        return width;
    }

    @SuppressWarnings("unused")
    public Container height(Constraint height) {
        this.height = height;
        return this;
    }

    public Constraint getHeight() {
        return height;
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
    public Container backgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @SuppressWarnings("unused")
    public Container padding(Constraint padding) {
        for(Direction direction : Direction.values()) {
            this.padding.put(direction, padding);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public Container padding(Direction direction, Constraint padding) {
        this.padding.put(direction, padding);
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

    public Container apply(Consumer<Container> consumer) {
        consumer.accept(this);
        return this;
    }

    public static class Constructor<T extends Container> extends Element.Constructor<T> {

        public void render(Renderer renderer, ElementData containerData) {
            Container container = this.getComponent();
            double width = containerData.getWidth();
            double height = containerData.getHeight();

            if(container.isBackgroundEnabled()) {
                renderer.getBackend().drawRectangle(containerData.getCalculatedX(), containerData.getCalculatedY(), width, height, container.getBackgroundColor());
            }

            Layout layout = container.getLayout();

            List<Pair<Element, ElementData>> layoutElementData = new ArrayList<>(layout.getConstructor().getStarterElementData(containerData));

            List<ElementData> appliedElements = new ArrayList<>();
            appliedElements.add(new ElementData(containerData, -width, 0, width, height * 16));
            appliedElements.add(new ElementData(containerData, width, 0, width, height * 16));
            appliedElements.add(new ElementData(containerData, 0, -height, width * 16, height));
            appliedElements.add(new ElementData(containerData, 0, height, width * 16, height));

            for(Pair<Element, ElementData> elementData : layoutElementData) {
                this.computeChildPaddings(elementData);
                for(int i = 0; i < 3; i++) {
                    this.adjustElementPosition(elementData.getValue(), appliedElements);
                    this.applyElementSizeRequests(elementData, appliedElements);
                }
                this.applyElementPositionRequests(elementData, appliedElements);

                appliedElements.add(elementData.getValue());

                elementData.getKey().getConstructor().render(renderer, elementData.getValue());
            }
        }

        protected void computeChildPaddings(Pair<Element, ElementData> elementData) {
            elementData.getKey().getConstructor().computePaddings(elementData.getValue());
        }

        @Override
        protected void computePaddings(ElementData elementData) {
            for(Element.Direction direction : Element.Direction.values()) {
                this.computePaddings(direction, elementData, this.getComponent().getPadding(direction));
            }
        }

        @Override
        public Constraint getWidthConstraint(ElementData elementData) {
            return this.getComponent().getWidth();
        }

        @Override
        public Constraint getHeightConstraint(ElementData elementData) {
            return this.getComponent().getHeight();
        }

        protected void adjustElementPosition(ElementData element, List<ElementData> appliedElements) {
            for(ElementData appliedElement : appliedElements) {
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

        protected void applyElementSizeRequests(Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            this.applyElementWidthRequest(elementData, appliedElements);
            this.applyElementHeightRequest(elementData, appliedElements);
        }

        private void applyElementWidthRequest(Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            Constraint widthConstraint = elementData.getKey().getConstructor().getWidthConstraint(elementData.getValue());
            elementData.getValue().setWidth(widthConstraint.getConstructor().getWidthValue(elementData.getValue(), appliedElements));
        }

        private void applyElementHeightRequest(Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            Constraint heightConstraint = elementData.getKey().getConstructor().getHeightConstraint(elementData.getValue());
            elementData.getValue().setHeight(heightConstraint.getConstructor().getHeightValue(elementData.getValue(), appliedElements));
        }

        protected void applyElementPositionRequests(Pair<Element, ElementData> element, List<ElementData> appliedElements) {
            this.applyElementXRequest(element, appliedElements);
            this.applyElementYRequest(element, appliedElements);
        }

        protected void applyElementXRequest(Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            Constraint xConstraint = elementData.getKey().getX();

            if(xConstraint instanceof Side) {
                Side.Direction direction = ((Side) xConstraint).getDirection();

                Pair<Double, Double> maxLeftRight = this.getMaximumExtension(elementData.getValue(), appliedElements, Axis.X);
                if(maxLeftRight == null) {
                    return;
                }

                if(direction == Side.Direction.NEGATIVE) {
                    elementData.getValue().setX(maxLeftRight.getKey() + elementData.getValue().getWidth() / 2);
                } else if(direction == Side.Direction.ZERO) {
                    elementData.getValue().setX((maxLeftRight.getKey() + maxLeftRight.getValue()) / 2);
                } else if(direction == Side.Direction.POSITIVE) {
                    elementData.getValue().setX(maxLeftRight.getValue() - elementData.getValue().getWidth() / 2);
                }
            }
        }

        protected void applyElementYRequest(Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            Constraint yConstraint = elementData.getKey().getY();

            if(yConstraint instanceof Side) {
                Side.Direction direction = ((Side) yConstraint).getDirection();

                Pair<Double, Double> maxTopBottom = this.getMaximumExtension(elementData.getValue(), appliedElements, Axis.Y);
                if(maxTopBottom == null) {
                    return;
                }

                if(direction == Side.Direction.NEGATIVE) {
                    elementData.getValue().setY(maxTopBottom.getKey() + elementData.getValue().getHeight() / 2);
                } else if(direction == Side.Direction.ZERO) {
                    elementData.getValue().setY((maxTopBottom.getKey() + maxTopBottom.getValue()) / 2);
                } else if(direction == Side.Direction.POSITIVE) {
                    elementData.getValue().setY(maxTopBottom.getValue() - elementData.getValue().getHeight() / 2);
                }
            }
        }

        private Pair<Double, Double> getMaximumExtension(ElementData element, List<ElementData> appliedElements, Axis axis) {
            if(axis == Axis.X) {
                double maxLeft = -Double.MAX_VALUE;
                double maxRight = Double.MAX_VALUE;

                for(ElementData appliedElement : appliedElements) {
                    MaxPaddingComputer maxPadding = new MaxPaddingComputer(element, appliedElement);

                    if(this.intersect(element, appliedElement, Axis.X)) {
                        if(element.getX() > appliedElement.getX()) {
                            maxLeft = Math.max(maxLeft, appliedElement.getX() + appliedElement.getWidth() / 2 + maxPadding.getLeft());
                        } else {
                            maxRight = Math.min(maxRight, appliedElement.getX() - appliedElement.getWidth() / 2 - maxPadding.getRight());
                        }
                    }
                }

                return new Pair<>(maxLeft, maxRight);
            } else if(axis == Axis.Y) {
                double maxTop = -Double.MAX_VALUE;
                double maxBottom = Double.MAX_VALUE;

                for(ElementData appliedElement : appliedElements) {
                    MaxPaddingComputer maxPadding = new MaxPaddingComputer(element, appliedElement);

                    if(this.intersect(element, appliedElement, Axis.Y)) {
                        if(element.getY() > appliedElement.getY()) {
                            maxTop = Math.max(maxTop, appliedElement.getY() + appliedElement.getHeight() / 2 + maxPadding.getTop());
                        } else {
                            maxBottom = Math.min(maxBottom, appliedElement.getY() - appliedElement.getHeight() / 2 - maxPadding.getBottom());
                        }
                    }
                }

                return new Pair<>(maxTop, maxBottom);
            }
            return null;
        }

    }

}
