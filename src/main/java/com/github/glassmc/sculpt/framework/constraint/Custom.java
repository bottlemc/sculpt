package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Custom extends Constraint {

    private final Constraint parent;
    private final Object handler;

    private Custom(Constraint parent, Object handler) {
        this.parent = parent;
        this.handler = handler;
        this.possibleConstructors.add(new Constructor<>());
    }

    public <T extends Constraint> Custom(T parent, Function<T, Object> handler) {
        this(parent, (Object) handler);
    }

    public Custom(Supplier<Object> supplier) {
        this(null, supplier);
    }

    public Constraint getParent() {
        return parent;
    }

    public Object getHandler() {
        return handler;
    }

    public static class Constructor<T extends Custom> extends Constraint.Constructor<T> {

        @Override
        public double getXValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (double) this.getValue();
        }

        @Override
        public double getYValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (double) this.getValue();
        }

        @Override
        public double getWidthValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (double) this.getValue();
        }

        @Override
        public double getHeightValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (double) this.getValue();
        }

        @Override
        public Color getColorValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return (Color) this.getValue();
        }

        @SuppressWarnings("unchecked")
        private Object getValue() {
            Object handler = this.getComponent().getHandler();
            if (handler instanceof Supplier) {
                return ((Supplier<?>) handler).get();
            } else if (handler instanceof Function) {
                return ((Function<Constraint, ?>) handler).apply(this.getComponent().getParent());
            }
            return null;
        }

    }

}
