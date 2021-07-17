package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class Constraint {

    protected final List<Constructor<?>> possibleConstructors = new ArrayList<>();
    private Constructor<?> constructor;

    public Constraint() {
        this.possibleConstructors.add(new Constructor<>());
    }

    public Constructor<?> getConstructor() {
        if(constructor == null) {
            constructor = this.possibleConstructors.get(this.possibleConstructors.size() - 1);
            constructor.setConstraint(this);
        }
        return constructor;
    }

    public static class Constructor<T extends Constraint> {

        private T constraint;

        public double getPaddingValue(ElementData elementData) {
            return 0;
        }

        @SuppressWarnings("unchecked")
        public void setConstraint(Constraint constraint) {
            this.constraint = (T) constraint;
        }

        public T getConstraint() {
            return constraint;
        }

    }

}
