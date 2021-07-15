package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.constraint.IConstraint;

public abstract class Element {

    private Container parent;

    private <T> T cast() {
        return (T) this;
    }

    public abstract IConstraint getX();
    public abstract IConstraint getY();
    public abstract IConstraint getPadding(Direction direction);

    public void setParent(Container parent) {
        this.parent = parent;
    }

    public Container getParent() {
        return parent;
    }

    public enum Direction {
        TOP, RIGHT, BOTTOM, LEFT
    }

}
