package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Component;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Renderer;

import java.util.List;

public abstract class Constraint extends Component {

    public Constraint() {
        this.possibleConstructors.add(new Constructor<>());
    }

    @Override
    public Constructor<? extends Constraint> getConstructor() {
        return (Constructor<? extends Constraint>) super.getConstructor();
    }

    public static class Constructor<T extends Constraint> extends Component.Constructor<T> {

        public double getPaddingValue(ElementData elementData) {
            return 0;
        }

        public double getXValue(ElementData elementData, List<ElementData> appliedElements) {
            return elementData.getX();
        }

        public double getYValue(ElementData elementData, List<ElementData> appliedElements) {
            return elementData.getY();
        }
        
        public double getWidthValue(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            return elementData.getWidth();
        }

        public double getHeightValue(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            return elementData.getHeight();
        }

        public double getFontSizeValue(ElementData elementData, List<ElementData> appliedElements) {
            return 0;
        }

        public Color getColorValue(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            return null;
        }

    }

}
