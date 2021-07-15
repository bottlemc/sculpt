package com.github.glassmc.sculpt.framework.constraint;

public class Absolute implements IConstraint {

    private final double value;

    public Absolute(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

}
