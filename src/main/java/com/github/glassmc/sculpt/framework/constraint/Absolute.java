package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.ElementData;

import java.util.List;

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
            return this.getComponent().getValue();
        }

        @Override
        public double getWidthValue(ElementData elementData, List<ElementData> appliedElements) {
            return super.getComponent().getValue();
        }

        @Override
        public double getHeightValue(ElementData elementData, List<ElementData> appliedElements) {
            return super.getComponent().getValue();
        }

    }

}
