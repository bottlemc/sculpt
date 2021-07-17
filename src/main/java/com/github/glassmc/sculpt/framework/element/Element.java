package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.Component;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.constraint.Constraint;

public abstract class Element extends Component {

    public Element() {
        this.possibleConstructors.add(new Constructor<>());
    }

    @Override
    public Constructor<? extends Element> getConstructor() {
        return (Constructor<? extends Element>) super.getConstructor();
    }

    public abstract Constraint getX();
    public abstract Constraint getY();

    public enum Direction {
        TOP, RIGHT, BOTTOM, LEFT
    }

    public static class Constructor<T extends Element> extends Component.Constructor<T> {

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

        public Constraint getWidthConstraint(ElementData elementData) {
            return null;
        }

        public Constraint getHeightConstraint(ElementData elementData) {
            return null;
        }

    }

}
