package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.element.Element;
import com.github.glassmc.sculpt.framework.util.Axis;

import java.util.List;

public class Flexible extends Constraint {

    public Flexible() {
        this.possibleConstructors.add(new Constructor<>());
    }

    public static class Constructor<T extends Flexible> extends Constraint.Constructor<T> {

        @Override
        public double getWidthValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Pair<Double, Double> maxLeftRight = this.getMaximumExtension(this.getComponent().getElement().getConstructor(), appliedElements, Axis.X);
            if(maxLeftRight != null) {
                double newWidth = maxLeftRight.getValue() - maxLeftRight.getKey();
                if(newWidth > 0) {
                    return newWidth;
                }
            }
            return this.getComponent().getElement().getConstructor().getWidth();
        }

        @Override
        public double getHeightValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Pair<Double, Double> maxTopBottom = this.getMaximumExtension(this.getComponent().getElement().getConstructor(), appliedElements, Axis.Y);
            if(maxTopBottom != null) {
                double newHeight = maxTopBottom.getValue() - maxTopBottom.getKey();
                if(newHeight > 0) {
                    return newHeight;
                }
            }
            return this.getComponent().getElement().getConstructor().getHeight();
        }

    }

}
