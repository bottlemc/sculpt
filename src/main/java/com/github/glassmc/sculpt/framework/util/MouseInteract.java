package com.github.glassmc.sculpt.framework.util;

import com.github.glassmc.sculpt.framework.element.Element;

public class MouseInteract<T extends Element> {

    private final T element;
    private final double x, y;

    public MouseInteract(T element, double x, double y) {
        this.element = element;
        this.x = x;
        this.y = y;
    }

    public Element getElement() {
        return element;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
