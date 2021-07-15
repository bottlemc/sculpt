package com.github.glassmc.sculpt.framework.constraint;

public class Relative implements IConstraint {

    private final double percent;
    private final boolean otherAxis;

    public Relative(double percent) {
        this(percent, false);
    }

    public Relative(double percent, boolean otherAxis) {
        this.percent = percent;
        this.otherAxis = otherAxis;
    }

    public double getPercent() {
        return percent;
    }

    public boolean isOtherAxis() {
        return otherAxis;
    }

}
