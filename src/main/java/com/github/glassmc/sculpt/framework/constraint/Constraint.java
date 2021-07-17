package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Component;
import com.github.glassmc.sculpt.framework.ElementData;

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

        public double getWidthValue(ElementData elementData, List<ElementData> appliedElements) {
            return 0;
        }

        public double getHeightValue(ElementData elementData, List<ElementData> appliedElements) {
            return 0;
        }

    }

}
