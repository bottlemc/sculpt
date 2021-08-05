package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Relative extends Constraint {

    private final Object percent;
    private final double offset;
    private final boolean otherAxis;

    private Relative(Object percent, double offset, boolean otherAxis) {
        this.possibleConstructors.add(new Constructor<>());
        this.percent = percent;
        this.offset = offset;
        this.otherAxis = otherAxis;
    }

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

    public Relative(Function<Element, Double> percent) {
        this(percent, 0, false);
    }

    public Object getPercent() {
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
            return base * this.getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getXValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getHeight() : parent.getWidth();
            return base * this.getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getYValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getWidth() : parent.getHeight();
            return base * this.getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getWidthValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getHeight() : parent.getWidth();
            return base * this.getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getHeightValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getWidth() : parent.getHeight();
            return base * this.getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getCornerRadiusValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Element.Constructor<?> parent = this.getComponent().getElement().getParent().getConstructor();
            double base = this.getComponent().isOtherAxis() ? parent.getHeight() : parent.getWidth();
            return base * this.getPercent() + this.getComponent().getOffset();
        }

        @Override
        public double getFontSizeValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getWidthValue(renderer, appliedElements);
        }

        @SuppressWarnings("unchecked")
        private double getPercent() {
            Object percent = this.getComponent().getPercent();
            if (percent instanceof Double) {
                return (double) percent;
            } else if (percent instanceof Function) {
                return (double) ((Function<Element, ?>) percent).apply(this.getComponent().getElement());
            }
            return 0;
        }

    }

}
