package com.github.glassmc.sculpt.framework;

import com.github.glassmc.sculpt.framework.backend.IRenderBackend;
import com.github.glassmc.sculpt.framework.constraint.*;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.element.Element;
import com.github.glassmc.sculpt.framework.element.Text;
import com.github.glassmc.sculpt.framework.layout.Layout;
import com.github.glassmc.sculpt.framework.layout.ListLayout;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;
import com.github.glassmc.sculpt.framework.util.Axis;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Renderer {

    private final IRenderBackend backend;

    public Renderer(IRenderBackend backend) {
        this.backend = backend;
    }

    public void render(Container container) {
        this.backend.preRender();

        Vector2D dimension = this.backend.getDimension();
        double width = dimension.getFirst();
        double height = dimension.getSecond();

        ElementData<Container> fakeParent = new ElementData<>(null, null, 0, 0, 1, 1);
        this.renderContainer(new ElementData<>(fakeParent, container, width / 2, height / 2, width, height));

        this.backend.postRender();
    }

    @SuppressWarnings("unchecked")
    private void render(ElementData<? extends Element> elementData) {
        if(elementData.element instanceof Container) {
            this.renderContainer((ElementData<Container>) elementData);
        } else if(elementData.element instanceof Text) {
            this.renderText((ElementData<Text>) elementData);
        }
    }

    private void renderContainer(ElementData<Container> containerData) {
        Container container = containerData.element;
        double width = containerData.width;
        double height = containerData.height;

        if(container.isBackgroundEnabled()) {
            this.backend.drawRectangle(containerData.getCalculatedX(), containerData.getCalculatedY(), width, height, container.getBackgroundColor());
        }

        Layout layout = container.getLayout();

        List<ElementData<? extends Element>> layoutElementData = new ArrayList<>();

        if(layout instanceof RegionLayout) {
            layoutElementData.addAll(getRegionStarterElementData(containerData, (RegionLayout) layout));
        } else if(layout instanceof ListLayout) {
            layoutElementData.addAll(getListStarterElementData(containerData, (ListLayout) layout));
        }

        List<ElementData<? extends Element>> appliedElements = new ArrayList<>();
        appliedElements.add(new ElementData<>(containerData, new Container(), -width, 0, width, height * 16));
        appliedElements.add(new ElementData<>(containerData, new Container(), width, 0, width, height * 16));
        appliedElements.add(new ElementData<>(containerData, new Container(), 0, -height, width * 16, height));
        appliedElements.add(new ElementData<>(containerData, new Container(), 0, height, width * 16, height));

        for(ElementData<? extends Element> elementData : layoutElementData) {
            this.computePaddings(containerData, elementData);
            for(int i = 0; i < 3; i++) {
                this.adjustElementPosition(elementData, appliedElements);
                this.applyElementSizeRequests(elementData, appliedElements);
            }
            this.applyElementPositionRequests(elementData, appliedElements);

            appliedElements.add(elementData);

            this.render(elementData);
        }
    }

    private void computePaddings(ElementData<Container> parent, ElementData<? extends Element> element) {
        for(Element.Direction direction : Element.Direction.values()) {
            IConstraint padding = element.element.getPadding(direction);
            double computedPadding = 0;

            if(padding instanceof Absolute) {
                computedPadding = ((Absolute) padding).getValue();
            } else if(padding instanceof Relative) {
                computedPadding = parent.width * ((Relative) padding).getPercent();
            }

            switch(direction) {
                case LEFT:
                    element.leftPadding = computedPadding;
                    break;
                case TOP:
                    element.topPadding = computedPadding;
                    break;
                case RIGHT:
                    element.rightPadding = computedPadding;
                    break;
                case BOTTOM:
                    element.bottomPadding = computedPadding;
                    break;
            }
        }
    }

    private List<ElementData<? extends Element>> getRegionStarterElementData(ElementData<Container> containerData, RegionLayout layout) {
        Map<RegionLayout.Region, List<Element>> regionMap = layout.getRegionMap();
        List<ElementData<? extends Element>> defaultElementData = new ArrayList<>();
        for(Element element : containerData.element.getChildren()) {
            RegionLayout.Region region = null;
            for(RegionLayout.Region tempRegion : regionMap.keySet()) {
                if(regionMap.get(tempRegion).contains(element)) {
                    region = tempRegion;
                }
            }

            if(region == null) {
                continue;
            }

            defaultElementData.add(new ElementData<>(containerData, element, region.getX() * containerData.width / 2, region.getY() * containerData.height / 2, 1, 1));
        }

        return defaultElementData;
    }

    private List<ElementData<? extends Element>> getListStarterElementData(ElementData<Container> containerData, ListLayout layout) {
        List<Element> elements = layout.getElements();
        List<ElementData<? extends Element>> defaultElementData = new ArrayList<>();
        int index = 0;
        for(Element element : elements) {
            defaultElementData.add(new ElementData<>(containerData, element, 0, -containerData.height / 2 + index, 1, 1));
            index += 1;
        }

        return defaultElementData;
    }

    private void adjustElementPosition(ElementData<? extends Element> element, List<ElementData<? extends Element>> appliedElements) {
        for(ElementData<? extends Element> appliedElement : appliedElements) {
            if(this.intersect(element, appliedElement, Axis.UNSPECIFIED)) {
                Map<Double, Axis> possibleChanges = new HashMap<>();

                MaxPaddingComputer maxPadding = new MaxPaddingComputer(element, appliedElement);

                possibleChanges.put((appliedElement.getRight() + maxPadding.left + element.width / 2) - element.x, Axis.X);
                possibleChanges.put((appliedElement.getLeft() - maxPadding.right - element.width / 2) - element.x, Axis.X);

                possibleChanges.put((appliedElement.getBottom() + maxPadding.top + element.height / 2) - element.y, Axis.Y);
                possibleChanges.put((appliedElement.getTop() - maxPadding.bottom - element.height / 2) - element.y, Axis.Y);

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
                    element.x += minChange.getKey();
                } else if(minChange.getValue() == Axis.Y) {
                    element.y += minChange.getKey();
                }
            }
        }
    }

    private void applyElementSizeRequests(ElementData<? extends Element> element, List<ElementData<? extends Element>> appliedElements) {
        this.applyElementWidthRequest(element, appliedElements);
        this.applyElementHeightRequest(element, appliedElements);
    }

    @SuppressWarnings("unchecked")
    private void applyElementWidthRequest(ElementData<? extends Element> element, List<ElementData<? extends Element>> appliedElements) {
        IConstraint widthConstraint = null;
        if(element.element instanceof Container) {
            widthConstraint = ((Container) element.element).getWidth();
        } else if(element.element instanceof Text) {
            widthConstraint = new Absolute(this.getBounds((ElementData<Text>) element).getWidth());
        }

        if(widthConstraint instanceof Absolute) {
            element.width = ((Absolute) widthConstraint).getValue();
        } else if(widthConstraint instanceof Relative) {
            Relative relative = (Relative) widthConstraint;
            element.width = (relative.isOtherAxis() ? element.parentData.height : element.parentData.width) * relative.getPercent();
        } else if(widthConstraint instanceof Flexible) {
            Pair<Double, Double> maxLeftRight = this.getMaximumExtension(element, appliedElements, Axis.X);
            if(maxLeftRight == null) {
                return;
            }
            double newWidth = maxLeftRight.getValue() - maxLeftRight.getKey();
            if(newWidth > 0) {
                element.width = newWidth;
            }
        } else if(widthConstraint instanceof Copy) {
            element.width = element.height;
        }
    }

    @SuppressWarnings("unchecked")
    private void applyElementHeightRequest(ElementData<? extends Element> element, List<ElementData<? extends Element>> appliedElements) {
        IConstraint heightConstraint = null;
        if(element.element instanceof Container) {
            heightConstraint = ((Container) element.element).getHeight();
        } else if(element.element instanceof Text) {
            heightConstraint = new Absolute(this.getBounds((ElementData<Text>) element).getHeight());
        }

        if(heightConstraint instanceof Absolute) {
            element.height = ((Absolute) heightConstraint).getValue();
        } else if(heightConstraint instanceof Relative) {
            Relative relative = (Relative) heightConstraint;
            element.height = (relative.isOtherAxis() ? element.parentData.width : element.parentData.height) * relative.getPercent();
        } else if(heightConstraint instanceof Flexible) {
            Pair<Double, Double> maxTopBottom = this.getMaximumExtension(element, appliedElements, Axis.Y);
            if(maxTopBottom == null) {
                return;
            }
            double newHeight = maxTopBottom.getValue() - maxTopBottom.getKey();
            if(newHeight > 0) {
                element.height = newHeight;
            }
        } else if(heightConstraint instanceof Copy) {
            element.height = element.width;
        }
    }

    private void applyElementPositionRequests(ElementData<? extends Element> element, List<ElementData<? extends Element>> appliedElements) {
        this.applyElementXRequest(element, appliedElements);
        this.applyElementYRequest(element, appliedElements);
    }

    private void applyElementXRequest(ElementData<? extends Element> element, List<ElementData<? extends Element>> appliedElements) {
        IConstraint xConstraint = element.element.getX();

        if(xConstraint instanceof Side) {
            Side.Direction direction = ((Side) xConstraint).getDirection();

            Pair<Double, Double> maxLeftRight = this.getMaximumExtension(element, appliedElements, Axis.X);
            if(maxLeftRight == null) {
                return;
            }

            if(direction == Side.Direction.NEGATIVE) {
                element.x = maxLeftRight.getKey() + element.width / 2;
            } else if(direction == Side.Direction.ZERO) {
                element.x = (maxLeftRight.getKey() + maxLeftRight.getValue()) / 2;
            } else if(direction == Side.Direction.POSITIVE) {
                element.x = maxLeftRight.getValue() - element.width / 2;
            }
        }
    }

    private void applyElementYRequest(ElementData<? extends Element> element, List<ElementData<? extends Element>> appliedElements) {
        IConstraint yConstraint = element.element.getY();

        if(yConstraint instanceof Side) {
            Side.Direction direction = ((Side) yConstraint).getDirection();

            Pair<Double, Double> maxLeftRight = this.getMaximumExtension(element, appliedElements, Axis.Y);
            if(maxLeftRight == null) {
                return;
            }

            if(direction == Side.Direction.NEGATIVE) {
                element.y = maxLeftRight.getKey() + element.height / 2;
            } else if(direction == Side.Direction.ZERO) {
                element.y = (maxLeftRight.getKey() + maxLeftRight.getValue()) / 2;
            } else if(direction == Side.Direction.POSITIVE) {
                element.y = maxLeftRight.getValue() - element.height / 2;
            }
        }
    }

    private Pair<Double, Double> getMaximumExtension(ElementData<? extends Element> element, List<ElementData<? extends Element>> appliedElements, Axis axis) {
        if(axis == Axis.X) {
            double maxLeft = -Double.MAX_VALUE;
            double maxRight = Double.MAX_VALUE;

            for(ElementData<? extends Element> appliedElement : appliedElements) {
                MaxPaddingComputer maxPadding = new MaxPaddingComputer(element, appliedElement);

                if(this.intersect(element, appliedElement, Axis.X)) {
                    if(element.x > appliedElement.x) {
                        maxLeft = Math.max(maxLeft, appliedElement.x + appliedElement.width / 2 + maxPadding.left);
                    } else {
                        maxRight = Math.min(maxRight, appliedElement.x - appliedElement.width / 2 - maxPadding.right);
                    }
                }
            }

            return new Pair<>(maxLeft, maxRight);
        } else if(axis == Axis.Y) {
            double maxTop = -Double.MAX_VALUE;
            double maxBottom = Double.MAX_VALUE;

            for(ElementData<? extends Element> appliedElement : appliedElements) {
                MaxPaddingComputer maxPadding = new MaxPaddingComputer(element, appliedElement);

                if(this.intersect(element, appliedElement, Axis.Y)) {
                    if(element.y > appliedElement.y) {
                        maxTop = Math.max(maxTop, appliedElement.y + appliedElement.height / 2 + maxPadding.top);
                    } else {
                        maxBottom = Math.min(maxBottom, appliedElement.y - appliedElement.height / 2 - maxPadding.bottom);
                    }
                }
            }

            return new Pair<>(maxTop, maxBottom);
        }
        return null;
    }

    private boolean intersect(ElementData<? extends Element> element1, ElementData<? extends Element> element2, Axis axis) {
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

    private void renderText(ElementData<Text> text) {
        Text textElement = text.element;
        this.backend.drawText(textElement.getFont().deriveFont((float) this.getFontSize(text)), textElement.getText(), text.getCalculatedX(), text.getCalculatedY(), textElement.getColor());
    }

    private Rectangle2D getBounds(ElementData<Text> elementData) {
        FontRenderContext fakeFontRendererContext = new FontRenderContext(null, false, false);
        Text textElement = elementData.element;
        double size = this.getFontSize(elementData);

        TextLayout textLayout = new TextLayout(textElement.getText(), textElement.getFont().deriveFont((float) size), fakeFontRendererContext);
        return textLayout.getBounds();
    }

    private double getFontSize(ElementData<Text> elementData) {
        Text textElement = elementData.element;
        IConstraint sizeConstraint = textElement.getSize();

        double size = 0;
        if(sizeConstraint instanceof Absolute) {
            size = ((Absolute) sizeConstraint).getValue();
        } else if(sizeConstraint instanceof Relative) {
            size = elementData.parentData.width * ((Relative) sizeConstraint).getPercent();
        }

        return size;
    }

    private static class ElementData<T extends Element> {

        private final ElementData<Container> parentData;
        private final T element;
        private double x, y;
        private double width, height;
        private double leftPadding, rightPadding, topPadding, bottomPadding;

        public ElementData(ElementData<Container> parentData, T element, double x, double y, double width, double height) {
            this.parentData = parentData;
            this.element = element;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public double getCalculatedX() {
            return (this.parentData != null ? this.parentData.getCalculatedX() : 0) + this.x;
        }

        public double getCalculatedY() {
            return (this.parentData != null ? this.parentData.getCalculatedY() : 0) + this.y;
        }

        public double getLeft() {
            return this.x - this.width / 2;
        }

        public double getRight() {
            return this.x + this.width / 2;
        }

        public double getTop() {
            return this.y - this.height / 2;
        }

        public double getBottom() {
            return this.y + this.height / 2;
        }

    }

    private static class MaxPaddingComputer {

        private final double left, top, right, bottom;

        public MaxPaddingComputer(ElementData<? extends Element> element1, ElementData<? extends Element> element2) {
            this.left = Math.max(element1.leftPadding, element2.rightPadding);
            this.top = Math.max(element1.topPadding, element2.bottomPadding);
            this.right = Math.max(element1.rightPadding, element2.leftPadding);
            this.bottom = Math.max(element1.bottomPadding, element2.bottomPadding);
        }

    }

}
