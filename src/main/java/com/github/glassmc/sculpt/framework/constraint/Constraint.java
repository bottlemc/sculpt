package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Component;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.List;

public abstract class Constraint extends Component {

    private Element element;

    public Constraint() {
        this.possibleConstructors.add(new Constructor<>());
    }

    @Override
    public Constructor<? extends Constraint> getConstructor() {
        return (Constructor<? extends Constraint>) super.getConstructor();
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public static class Constructor<T extends Constraint> extends Component.Constructor<T> {

        public double getPaddingValue() {
            return 0;
        }

        public double getXValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getComponent().getElement().getConstructor().getX();
        }

        public double getYValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getComponent().getElement().getConstructor().getY();
        }
        
        public double getWidthValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getComponent().getElement().getConstructor().getWidth();
        }

        public double getHeightValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getComponent().getElement().getConstructor().getHeight();
        }

        public double getCornerRadiusValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return 0;
        }

        public double getFontSizeValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return 0;
        }

        public Color getColorValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return null;
        }

    }

}
