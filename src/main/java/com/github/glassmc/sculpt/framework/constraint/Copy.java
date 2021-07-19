package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.List;

public class Copy extends Constraint {

    public Copy() {
        this.possibleConstructors.add(new Constructor<>());
    }

    public static class Constructor<T extends Copy> extends Constraint.Constructor<T> {

        @Override
        public double getWidthValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getComponent().getElement().getConstructor().getHeight();
        }

        @Override
        public double getHeightValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getComponent().getElement().getConstructor().getWidth();
        }

    }

}
