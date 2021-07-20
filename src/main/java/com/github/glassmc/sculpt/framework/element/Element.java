package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.Component;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.constraint.Constraint;

import java.util.List;

public abstract class Element extends Component {

    private Container parent;

    public Element() {
        this.possibleConstructors.add(new Constructor<>());
    }

    @Override
    public Constructor<? extends Element> getConstructor() {
        return (Constructor<? extends Element>) super.getConstructor();
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    public Container getParent() {
        return parent;
    }

    public enum Direction {
        TOP, RIGHT, BOTTOM, LEFT
    }

    public static class Constructor<T extends Element> extends Component.Constructor<T> {

        private double x, y;
        private double width, height;

        private double leftPadding, rightPadding, topPadding, bottomPadding;

        public void render(Renderer renderer, List<Constructor<?>> appliedElements) {

        }

        protected void computePaddings() {

        }

        protected void computePaddings(Direction direction, Constraint padding) {
            double computedPadding = padding.getConstructor().getPaddingValue();

            switch(direction) {
                case LEFT:
                    this.leftPadding = computedPadding;
                    break;
                case TOP:
                    this.topPadding = computedPadding;
                    break;
                case RIGHT:
                    this.rightPadding = computedPadding;
                    break;
                case BOTTOM:
                    this.bottomPadding = computedPadding;
                    break;
            }
        }

        public Constraint getXConstraint() {
            return null;
        }

        public Constraint getYConstraint() {
            return null;
        }

        public double getWidth(Renderer renderer, List<Constructor<?>> appliedElements) {
            return 0;
        }

        public double getHeight(Renderer renderer, List<Constructor<?>> appliedElements) {
            return 0;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getX() {
            return x;
        }

        public double getCalculatedX() {
            return (this.getComponent().getParent() != null ? this.getComponent().getParent().getConstructor().getCalculatedX() : 0) + this.x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getY() {
            return y;
        }

        public double getCalculatedY() {
            return (this.getComponent().getParent() != null ? this.getComponent().getParent().getConstructor().getCalculatedY() : 0) + this.y;
        }

        public void setWidth(double width) {
            this.width = width;
        }

        public double getWidth() {
            return width;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public double getHeight() {
            return height;
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

        public double getLeftPadding() {
            return leftPadding;
        }

        public double getRightPadding() {
            return rightPadding;
        }

        public double getBottomPadding() {
            return bottomPadding;
        }

        public double getTopPadding() {
            return topPadding;
        }

    }

}
