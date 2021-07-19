package com.github.glassmc.sculpt.framework.modifier.returner;

import com.github.glassmc.sculpt.framework.Component;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Renderer;

public class PercentReturner extends Component {

    public PercentReturner() {
        this.possibleConstructors.add(new Constructor<>());
    }

    @Override
    public PercentReturner.Constructor<?> getConstructor() {
        return (Constructor<?>) super.getConstructor();
    }

    public static class Constructor<T extends PercentReturner> extends Component.Constructor<T> {

        public void update(Renderer renderer, ElementData elementData) {

        }

        public double getPercent() {
            return 0;
        }

    }

}
