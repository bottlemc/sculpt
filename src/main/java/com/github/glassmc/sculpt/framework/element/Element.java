package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.constraint.Constraint;

import java.util.ArrayList;
import java.util.List;

public abstract class Element {

    protected final List<Constructor<?>> possibleConstructors = new ArrayList<>();
    private Constructor<?> constructor;

    public Element() {
        this.possibleConstructors.add(new Constructor<>());
    }

    public abstract Constraint getX();
    public abstract Constraint getY();

    public Constructor<? extends Element> getConstructor() {
        if(constructor == null) {
            constructor = this.possibleConstructors.get(this.possibleConstructors.size() - 1);
            constructor.setElement(this);
        }
        return constructor;
    }

    public enum Direction {
        TOP, RIGHT, BOTTOM, LEFT
    }

    public static class Constructor<T extends Element> {

        private T element;

        public void render(Renderer renderer, ElementData elementData) {

        }

        protected void computePaddings(ElementData elementData) {

        }

        protected void computePaddings(Direction direction, ElementData elementData, Constraint padding) {
            double computedPadding = padding.getConstructor().getPaddingValue(elementData);

            switch(direction) {
                case LEFT:
                    elementData.setLeftPadding(computedPadding);
                    break;
                case TOP:
                    elementData.setTopPadding(computedPadding);
                    break;
                case RIGHT:
                    elementData.setRightPadding(computedPadding);
                    break;
                case BOTTOM:
                    elementData.setBottomPadding(computedPadding);
                    break;
            }
        }

        public T getElement() {
            return element;
        }

        @SuppressWarnings("unchecked")
        public void setElement(Element element) {
            this.element = (T) element;
        }

    }

}
