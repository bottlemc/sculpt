package com.github.glassmc.sculpt.framework;

public class MouseAction {

    private final Type type;
    private final Vector2D location;

    public MouseAction(Type type, Vector2D location) {
        this.type = type;
        this.location = location;
    }

    public Type getType() {
        return type;
    }

    public Vector2D getLocation() {
        return location;
    }

    public enum Type {
        CLICK,
        RELEASE
    }

}
