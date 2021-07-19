package com.github.glassmc.sculpt.framework.modifier;

import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.constraint.Constraint;
import com.github.glassmc.sculpt.framework.modifier.returner.PercentReturner;

import java.util.List;

public class Resize extends Modifier {

    private final PercentReturner returner;
    private final Constraint initialSize, finalSize;

    public Resize(PercentReturner returner, Constraint initialSize, Constraint finalSize) {
        this.possibleConstructors.add(new Constructor<>());
        this.returner = returner;
        this.initialSize = initialSize;
        this.finalSize = finalSize;
    }

    public PercentReturner getReturner() {
        return returner;
    }

    public Constraint getInitialSize() {
        return initialSize;
    }

    public Constraint getFinalSize() {
        return finalSize;
    }

    public static class Constructor<T extends Resize> extends Modifier.Constructor<T> {

        @Override
        public double getWidth(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            this.getComponent().getReturner().getConstructor().update(renderer, elementData);
            double percent = this.getComponent().getReturner().getConstructor().getPercent();
            double initialValue = this.getComponent().getInitialSize().getConstructor().getWidthValue(elementData, appliedElements);
            double finalValue = this.getComponent().getFinalSize().getConstructor().getWidthValue(elementData, appliedElements);

            return (finalValue - initialValue) * percent + initialValue;
        }

        @Override
        public double getHeight(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            return super.getHeight(renderer, elementData, appliedElements);
        }

    }

}
