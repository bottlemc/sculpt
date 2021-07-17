package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.ElementData;

public class Relative extends Constraint {

    private final double percent;
    private final boolean otherAxis;

    public Relative(double percent) {
        this(percent, false);
        this.possibleConstructors.add(new Constructor<>());
    }

    public Relative(double percent, boolean otherAxis) {
        this.percent = percent;
        this.otherAxis = otherAxis;
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
            return elementData.getParentData().getWidth() * this.getConstraint().getPercent();
        }

    }

}
