package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Component;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.util.Axis;

import java.util.List;

public class Flexible extends Constraint {

    public Flexible() {
        this.possibleConstructors.add(new Constructor<>());
    }

    public static class Constructor<T extends Flexible> extends Constraint.Constructor<T> {

        @Override
        public double getWidthValue(ElementData elementData, List<ElementData> appliedElements) {
            Pair<Double, Double> maxLeftRight = this.getMaximumExtension(elementData, appliedElements, Axis.X);
            if(maxLeftRight != null) {
                double newWidth = maxLeftRight.getValue() - maxLeftRight.getKey();
                if(newWidth > 0) {
                    return newWidth;
                }
            }
            return elementData.getWidth();
        }

        @Override
        public double getHeightValue(ElementData elementData, List<ElementData> appliedElements) {
            Pair<Double, Double> maxTopBottom = this.getMaximumExtension(elementData, appliedElements, Axis.Y);
            if(maxTopBottom != null) {
                double newHeight = maxTopBottom.getValue() - maxTopBottom.getKey();
                if(newHeight > 0) {
                    return newHeight;
                }
            }
            return elementData.getWidth();
        }

        private Pair<Double, Double> getMaximumExtension(ElementData element, List<ElementData> appliedElements, Axis axis) {
            if(axis == Axis.X) {
                double maxLeft = -Double.MAX_VALUE;
                double maxRight = Double.MAX_VALUE;

                for(ElementData appliedElement : appliedElements) {
                    Component.Constructor.MaxPaddingComputer maxPadding = new Container.Constructor.MaxPaddingComputer(element, appliedElement);

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
                    Container.Constructor.MaxPaddingComputer maxPadding = new Container.Constructor.MaxPaddingComputer(element, appliedElement);

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
