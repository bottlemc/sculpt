package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.ElementData;

import java.util.List;

public class Copy extends Constraint {

    public Copy() {
        this.possibleConstructors.add(new Constructor<>());
    }

    public static class Constructor<T extends Copy> extends Constraint.Constructor<T> {

        @Override
        public double getWidthValue(ElementData elementData, List<ElementData> appliedElements) {
            return elementData.getHeight();
        }

        @Override
        public double getHeightValue(ElementData elementData, List<ElementData> appliedElements) {
            return elementData.getWidth();
        }

    }

}
