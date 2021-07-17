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
            Container container = this.getElement();
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
                this.computePaddings(direction, elementData, this.getElement().getPadding(direction));
            }
        }

        protected void adjustElementPosition(ElementData element, List<ElementData> appliedElements) {
            for(ElementData appliedElement : appliedElements) {
                if(this.intersect(element, appliedElement, Axis.UNSPECIFIED)) {
                    Map<Double, Axis> possibleChanges = new HashMap<>();

                    MaxPaddingComputer maxPadding = new MaxPaddingComputer(element, appliedElement);

                    possibleChanges.put((appliedElement.getRight() + maxPadding.left + element.getWidth() / 2) - element.getX(), Axis.X);
                    possibleChanges.put((appliedElement.getLeft() - maxPadding.right - element.getWidth() / 2) - element.getX(), Axis.X);

                    possibleChanges.put((appliedElement.getBottom() + maxPadding.top + element.getHeight() / 2) - element.getY(), Axis.Y);
                    possibleChanges.put((appliedElement.getTop() - maxPadding.bottom - element.getHeight() / 2) - element.getY(), Axis.Y);

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
            Constraint widthConstraint = null;
            if(elementData.getKey() instanceof Container) {
                widthConstraint = ((Container) elementData.getKey()).getWidth();
            } else if(elementData.getKey() instanceof Text) {
                widthConstraint = new Absolute(this.getBounds(elementData).getWidth());
            }

            if(widthConstraint instanceof Absolute) {
                elementData.getValue().setWidth(((Absolute) widthConstraint).getValue());
            } else if(widthConstraint instanceof Relative) {
                Relative relative = (Relative) widthConstraint;
                elementData.getValue().setWidth((relative.isOtherAxis() ? elementData.getValue().getParentData().getHeight() : elementData.getValue().getParentData().getWidth()) * relative.getPercent());
            } else if(widthConstraint instanceof Flexible) {
                Pair<Double, Double> maxLeftRight = this.getMaximumExtension(elementData.getValue(), appliedElements, Axis.X);
                if(maxLeftRight == null) {
                    return;
                }
                double newWidth = maxLeftRight.getValue() - maxLeftRight.getKey();
                if(newWidth > 0) {
                    elementData.getValue().setWidth(newWidth);
                }

            } else if(widthConstraint instanceof Copy) {
                elementData.getValue().setWidth(elementData.getValue().getHeight());
            }
        }

        private void applyElementHeightRequest(Pair<Element, ElementData> elementData, List<ElementData> appliedElements) {
            Constraint heightConstraint = null;
            if(elementData.getKey() instanceof Container) {
                heightConstraint = ((Container) elementData.getKey()).getHeight();
            } else if(elementData.getKey() instanceof Text) {
                heightConstraint = new Absolute(this.getBounds(elementData).getHeight());
            }

            if(heightConstraint instanceof Absolute) {
                elementData.getValue().setHeight(((Absolute) heightConstraint).getValue());
            } else if(heightConstraint instanceof Relative) {
                Relative relative = (Relative) heightConstraint;
                elementData.getValue().setHeight((relative.isOtherAxis() ? elementData.getValue().getParentData().getWidth() : elementData.getValue().getParentData().getHeight()) * relative.getPercent());
            } else if(heightConstraint instanceof Flexible) {
                Pair<Double, Double> maxTopBottom = this.getMaximumExtension(elementData.getValue(), appliedElements, Axis.Y);
                if(maxTopBottom == null) {
                    return;
                }
                double newHeight = maxTopBottom.getValue() - maxTopBottom.getKey();
                if(newHeight > 0) {
                    elementData.getValue().setHeight(newHeight);
                }
            } else if(heightConstraint instanceof Copy) {
                elementData.getValue().setHeight(elementData.getValue().getWidth());
            }
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
                            maxLeft = Math.max(maxLeft, appliedElement.getX() + appliedElement.getWidth() / 2 + maxPadding.left);
                        } else {
                            maxRight = Math.min(maxRight, appliedElement.getX() - appliedElement.getWidth() / 2 - maxPadding.right);
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
                            maxTop = Math.max(maxTop, appliedElement.getY() + appliedElement.getHeight() / 2 + maxPadding.top);
                        } else {
                            maxBottom = Math.min(maxBottom, appliedElement.getY() - appliedElement.getHeight() / 2 - maxPadding.bottom);
                        }
                    }
                }

                return new Pair<>(maxTop, maxBottom);
            }
            return null;
        }

        protected boolean intersect(ElementData element1, ElementData element2, Axis axis) {
            double lenience = 0.01;
            MaxPaddingComputer maxPadding = new MaxPaddingComputer(element1, element2);

            boolean intersectX = (element2.getBottom() > element1.getTop() - maxPadding.top + lenience) &&
                    (element1.getBottom() + maxPadding.bottom - lenience > element2.getTop());
            boolean intersectY = (element2.getRight() > element1.getLeft() - maxPadding.left + lenience) &&
                    (element1.getRight() + maxPadding.right - lenience > element2.getLeft());

            if(axis == Axis.X) {
                return intersectX;
            } else if(axis == Axis.Y) {
                return intersectY;
            }
            return intersectX && intersectY;
        }

        private Rectangle2D getBounds(Pair<Element, ElementData> elementData) {
            FontRenderContext fakeFontRendererContext = new FontRenderContext(null, false, false);
            Text textElement = (Text) elementData.getKey();
            double size = this.getFontSize(elementData);

            TextLayout textLayout = new TextLayout(textElement.getText(), textElement.getFont().deriveFont((float) size), fakeFontRendererContext);
            return textLayout.getBounds();
        }

        private double getFontSize(Pair<Element, ElementData> elementData) {
            Text textElement = (Text) elementData.getKey();
            Constraint sizeConstraint = textElement.getSize();

            double size = 0;
            if(sizeConstraint instanceof Absolute) {
                size = ((Absolute) sizeConstraint).getValue();
            } else if(sizeConstraint instanceof Relative) {
                size = elementData.getValue().getParentData().getWidth() * ((Relative) sizeConstraint).getPercent();
            }

            return size;
        }

        private static class MaxPaddingComputer {

            private final double left, top, right, bottom;

            public MaxPaddingComputer(ElementData element1, ElementData element2) {
                this.left = Math.max(element1.getLeftPadding(), element2.getRightPadding());
                this.top = Math.max(element1.getTopPadding(), element2.getBottomPadding());
                this.right = Math.max(element1.getRightPadding(), element2.getLeftPadding());
                this.bottom = Math.max(element1.getBottomPadding(), element2.getTopPadding());
            }

        }

    }

}
