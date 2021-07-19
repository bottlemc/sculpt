package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.Component;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.List;

public abstract class Layout extends Component {

    private Container container;

    public Layout() {
       this.possibleConstructors.add(new Constructor<>());
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Container getContainer() {
        return this.container;
    }

    @Override
    public Layout.Constructor<? extends Layout> getConstructor() {
        return (Constructor<? extends Layout>) super.getConstructor();
    }

    public static class Constructor<T extends Layout> extends Component.Constructor<T> {

        public List<Pair<Element, ElementData>> getStarterElementData(ElementData containerData) {
            return null;
        }

    }

}
