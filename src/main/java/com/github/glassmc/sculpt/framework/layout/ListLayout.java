package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.ArrayList;
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
        public List<Pair<Element, ElementData>> getStarterElementData(ElementData containerData) {
            List<Element> elements = this.getComponent().getElements();
            List<Pair<Element, ElementData>> defaultElementData = new ArrayList<>();
            int index = 0;
            for(Element element : elements) {
                defaultElementData.add(new Pair<>(element, new ElementData(containerData, 0, -containerData.getHeight() / 2 + index, 1, 1)));
                index += 1;
            }

            return defaultElementData;
        }

    }

}
