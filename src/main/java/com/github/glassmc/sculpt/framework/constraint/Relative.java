package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.List;

public class Relative extends Constraint {

    private final double percent;
    private final double offset;
    private final boolean otherAxis;

    public Relative(double percent, double offset, boolean otherAxis) {
        this.possibleConstructors.add(new Constructor<>());
        this.percent = percent;
        this.offset = offset;
        this.otherAxis = otherAxis;
    }

    public Relative(double percent, double offset) {
        this(percent, offset, false);
    }

    public Relative(double percent) {
        this(percent, 0);
    }

    public double getPercent() {
        return percent;
    }

    public double getOffset() {
        return offset;
    }

    public boolean isOtherAxis() {
        return otherAxis;
    }

    public static class Constructor<T extends Relative> extends Constraint.Constructor<T> {

        @Override
        public double getPaddingValue() {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getHeight() : parent.getWidth();
            return base * this.getComponent().getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getXValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getHeight() : parent.getWidth();
            return base * this.getComponent().getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getYValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getWidth() : parent.getHeight();
            return base * this.getComponent().getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getWidthValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getHeight() : parent.getWidth();
            return base * this.getComponent().getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getHeightValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getWidth() : parent.getHeight();
            return base * this.getComponent().getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getCornerRadiusValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getHeight() : parent.getWidth();
            return base * this.getComponent().getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getFontSizeValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getWidthValue(renderer, appliedElements);
        }

    }

}
