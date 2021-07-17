package com.github.glassmc.sculpt.framework;

import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.util.Axis;

import java.util.ArrayList;
import java.util.List;

public class Component {

    protected final List<Constructor<?>> possibleConstructors = new ArrayList<>();
    private Constructor<?> constructor;

    public Component() {
        this.possibleConstructors.add(new Constructor<>());
    }

    public Constructor<?> getConstructor() {
        if(constructor == null) {
            constructor = possibleConstructors.get(possibleConstructors.size() - 1);
            constructor.setComponent(this);
        }
        return constructor;
    }

    public static class Constructor<T extends Component> {

        private T component;

        public T getComponent() {
            return component;
        }

        @SuppressWarnings("unchecked")
        public void setComponent(Component component) {
            this.component = (T) component;
        }

        protected Pair<Double, Double> getMaximumExtension(ElementData element, List<ElementData> appliedElements, Axis axis) {
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

        protected static class MaxPaddingComputer {

            private final double left, top, right, bottom;

            public MaxPaddingComputer(ElementData element1, ElementData element2) {
                this.left = Math.max(element1.getLeftPadding(), element2.getRightPadding());
                this.top = Math.max(element1.getTopPadding(), element2.getBottomPadding());
                this.right = Math.max(element1.getRightPadding(), element2.getLeftPadding());
                this.bottom = Math.max(element1.getBottomPadding(), element2.getTopPadding());
            }

            public double getLeft() {
                return left;
            }

            public double getTop() {
                return top;
            }

            public double getRight() {
                return right;
            }

            public double getBottom() {
                return bottom;
            }

        }

    }

}
