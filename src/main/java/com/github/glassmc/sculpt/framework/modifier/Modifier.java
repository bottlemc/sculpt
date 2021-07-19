package com.github.glassmc.sculpt.framework.modifier;

import com.github.glassmc.sculpt.framework.Component;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.element.Element;

public class Modifier extends Component {

    private Element element;

    public Modifier() {
        this.possibleConstructors.add(new Constructor<>());
    }

    @Override
    public Modifier.Constructor<? extends Modifier> getConstructor() {
        return (Constructor<? extends Modifier>) super.getConstructor();
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public static class Constructor<T extends Modifier> extends Component.Constructor<T> {

        public void update(Renderer renderer, ElementData elementData) {

        }

        public double getPercent() {
            return 0;
        }

    }

}
