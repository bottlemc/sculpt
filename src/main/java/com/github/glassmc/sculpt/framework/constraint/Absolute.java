package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.List;

public class Absolute extends Constraint {

    private final Object value;

    public Absolute(Object value) {
        this.possibleConstructors.add(new Constructor<>());
        this.value = value;
    }

    public Absolute(double value) {
        this((Object) value);
    }

    public Absolute(Color value) {
        this((Object) value);
    }

    public Object getValue() {
        return value;
    }

    public static class Constructor<T extends Absolute> extends Constraint.Constructor<T> {

        @Override
        public double getPaddingValue() {
            return (double) this.getComponent().getValue();
        }

        @Override
        public double getXValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (double) super.getComponent().getValue();
        }

        @Override
        public double getYValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (double) super.getComponent().getValue();
        }

        @Override
        public double getWidthValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (double) super.getComponent().getValue();
        }

        @Override
        public double getHeightValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (double) super.getComponent().getValue();
        }

        @Override
        public double getCornerRadiusValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (double) super.getComponent().getValue();
        }

        @Override
        public Color getColorValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (Color) super.getComponent().getValue();
        }

    }

}
