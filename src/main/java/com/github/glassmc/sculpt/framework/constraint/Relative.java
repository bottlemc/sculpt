package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.ElementData;

import java.util.List;

public class Relative extends Constraint {

    private final double percent;
    private final boolean otherAxis;

    public Relative(double percent, boolean otherAxis) {
        this.possibleConstructors.add(new Constructor<>());
        this.percent = percent;
        this.otherAxis = otherAxis;
    }

    public Relative(double percent) {
        this(percent, false);
    }

    public double getPercent() {
        return percent;
    }

    public boolean isOtherAxis() {
        return otherAxis;
    }

    public static class Constructor<T extends Relative> extends Constraint.Constructor<T> {

        @Override
        public double getPaddingValue(ElementData elementData) {
            return elementData.getParentData().getWidth() * this.getComponent().getPercent();
        }

        @Override
        public double getWidthValue(ElementData elementData, List<ElementData> appliedElements) {
            double base = this.getComponent().isOtherAxis() ? elementData.getParentData().getHeight() : elementData.getParentData().getWidth();
            return base * this.getComponent().getPercent();
        }

        @Override
        public double getHeightValue(ElementData elementData, List<ElementData> appliedElements) {
            double base = this.getComponent().isOtherAxis() ? elementData.getParentData().getWidth() : elementData.getParentData().getHeight();
            return base * this.getComponent().getPercent();
        }

    }

}
