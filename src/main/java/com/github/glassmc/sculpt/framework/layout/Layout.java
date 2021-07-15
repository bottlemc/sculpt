package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.element.Container;

public abstract class Layout {

    private Container container;

    public void setContainer(Container container) {
        this.container = container;
    }

    public Container getContainer() {
        return this.container;
    }

}
