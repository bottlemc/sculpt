package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.List;

public abstract class Layout {

    private final Constructor<?> constructor;

    private Container container;

    public Layout(Constructor<?> constructor) {
        this.constructor = constructor;
        this.constructor.setLayout(this);
    }

    public Layout() {
        this(new Constructor<>());
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Container getContainer() {
        return this.container;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public static class Constructor<T extends Layout> {

        private T layout;

        public List<Pair<Element, ElementData>> getStarterElementData(ElementData containerData) {
            return null;
        }

        public T getLayout() {
            return layout;
        }

        @SuppressWarnings("unchecked")
        public void setLayout(Layout layout) {
            this.layout = (T) layout;
        }

    }

}
