package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.ElementData;

public class Absolute extends Constraint {

    private final double value;

    public Absolute(double value) {
        this.possibleConstructors.add(new Constructor<>());
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static class Constructor<T extends Absolute> extends Constraint.Constructor<T> {

        @Override
        public double getPaddingValue(ElementData elementData) {
            return this.getConstraint().getValue();
        }

    }

}
