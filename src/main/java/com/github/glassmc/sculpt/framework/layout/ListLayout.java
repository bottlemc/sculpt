package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.element.Element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListLayout extends Layout {

    private final Type type;
    private final List<Element> elements = new ArrayList<>();

    public ListLayout(Type type) {
        this.possibleConstructors.add(new Constructor<>());
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

    public static class Constructor<T extends ListLayout> extends Layout.Constructor<T> {

        @Override
        public List<Element.Constructor<?>> getDefaultElements() {
            List<Element> elements = new ArrayList<>(this.getComponent().getContainer().getChildren());
            List<Element> elementList = this.getComponent().getElements();

            elementList.sort(new Comparator<Element>() {
                @Override
                public int compare(Element o1, Element o2) {
                    return Integer.compare(elementList.indexOf(o1), elementList.indexOf(o2));
                }
            });

            List<Element.Constructor<?>> defaultElements = new ArrayList<>();
            int index = 0;
            for(Element element : elements) {
                defaultElements.add(element.getConstructor());
                element.getConstructor().setY(-this.getComponent().getContainer().getConstructor().getHeight() / 2 + index);
                index += 1;
            }

            return defaultElements;
        }

    }

}
