package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.constraint.*;
import com.github.glassmc.sculpt.framework.layout.Layout;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;
import com.github.glassmc.sculpt.framework.modifier.Modifier;
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
    private boolean staticBackground = true;
    private Color backgroundColor = new Color(1., 1., 1.);
    private Modifier backgroundColorModifier;

    private boolean staticWidth, staticHeight;
    private Constraint width = new Flexible(), height = new Flexible();
    private Modifier widthModifier, heightModifier;
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

    public Constraint getX() {
        return x;
    }

    @SuppressWarnings("unused")
    public Container y(Constraint y) {
        this.y = y;
        return this;
    }

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
        this.staticWidth = true;
        return this;
    }

    public Container width(Modifier widthModifier) {
        this.widthModifier = widthModifier;
        this.staticWidth = false;
        return this;
    }

    public boolean isStaticWidth() {
        return staticWidth;
    }

    public Constraint getWidth() {
        return width;
    }

    public Modifier getWidthModifier() {
        return widthModifier;
    }

    @SuppressWarnings("unused")
    public Container height(Constraint height) {
        this.height = height;
        this.staticHeight = true;
        return this;
    }

    public Container height(Modifier heightModifier) {
        this.heightModifier = heightModifier;
        this.staticHeight = false;
        return this;
    }

    public boolean isStaticHeight() {
        return staticHeight;
    }

    public Constraint getHeight() {
        return height;
    }

    public Modifier getHeightModifier() {
        return heightModifier;
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
        this.staticBackground = true;
        return this;
    }

    public Container backgroundColor(Modifier backgroundColorModifier) {
        this.backgroundColorModifier = backgroundColorModifier;
        this.staticBackground = false;
        return this;
    }

    public Modifier getBackgroundColorModifier() {
        return backgroundColorModifier;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isStaticBackground() {
        return staticBackground;
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
                Color color;
                if(container.isStaticBackground()) {
                    color = container.getBackgroundColor();
                } else {
                    container.getBackgroundColorModifier().getConstructor().update(renderer, containerData);
                    color = container.getBackgroundColorModifier().getConstructor().getColor(renderer, containerData);
                }
                renderer.getBackend().drawRectangle(containerData.getCalculatedX(), containerData.getCalculatedY(), width, height, color);
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
                    this.applyElementSizeRequests(renderer, elementData, appliedElements);
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
        public double getWidth(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            if(this.getComponent().isStaticWidth()) {
                return this.getComponent().getWidth().getConstructor().getWidthValue(elementData, appliedElements);
            } else {
                return this.getComponent().getWidthModifier().getConstructor().getWidth(renderer, elementData, appliedElements);
            }
        }

        @Override
        public double getHeight(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            if(this.getComponent().isStaticHeight()) {
                return this.getComponent().getHeight().getConstructor().getHeightValue(elementData, appliedElements);
            } else {
                return this.getComponent().getHeightModifier().getConstructor().getHeight(renderer, elementData, appliedElements);
            }
        }

        @Override
        public Constraint getXConstraint(ElementData elementData) {
            return this.getComponent().getX();
        }

        @Override
        public Constraint getYConstraint(ElementData elementData) {
            return this.getComponent().getY();
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

        protected void applyElementSizeRequests(Renderer renderer, Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            this.applyElementWidthRequest(renderer, elementData, appliedElements);
            this.applyElementHeightRequest(renderer, elementData, appliedElements);
        }

        private void applyElementWidthRequest(Renderer renderer, Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            double width = elementData.getKey().getConstructor().getWidth(renderer, elementData.getValue(), appliedElements);
            elementData.getValue().setWidth(width);
        }

        private void applyElementHeightRequest(Renderer renderer, Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            double height = elementData.getKey().getConstructor().getHeight(renderer, elementData.getValue(), appliedElements);
            elementData.getValue().setHeight(height);
        }

        protected void applyElementPositionRequests(Pair<Element, ElementData> element, List<ElementData> appliedElements) {
            this.applyElementXRequest(element, appliedElements);
            this.applyElementYRequest(element, appliedElements);
        }

        protected void applyElementXRequest(Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            Constraint xConstraint = elementData.getKey().getConstructor().getXConstraint(elementData.getValue());
            elementData.getValue().setX(xConstraint.getConstructor().getXValue(elementData.getValue(), appliedElements));
        }

        protected void applyElementYRequest(Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            Constraint yConstraint = elementData.getKey().getConstructor().getYConstraint(elementData.getValue());
            elementData.getValue().setY(yConstraint.getConstructor().getYValue(elementData.getValue(), appliedElements));
        }

    }

}
