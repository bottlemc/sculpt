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

    }

}
