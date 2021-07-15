package com.github.glassmc.sculpt.framework.constraint;

public class Side implements IConstraint {

    private final Direction direction;

    public Side(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public enum Direction {
        NEGATIVE, POSITIVE, ZERO
    }

}
