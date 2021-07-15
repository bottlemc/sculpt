package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.element.Element;

import java.util.ArrayList;
import java.util.List;

public class ListLayout extends Layout {

    private final Type type;
    private final List<Element> elements = new ArrayList<>();

    public ListLayout(Type type) {
        this.type = type;
    }

    public ListLayout add(Element element) {
        this.getContainer().add(element);
        this.elements.add(element);
        return this;
    }

    public List<Element> getElements() {
        return elements;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        HORIZONTAL,
        VERTICAL
    }

}
