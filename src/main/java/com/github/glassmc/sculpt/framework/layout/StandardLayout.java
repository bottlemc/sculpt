package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.element.Element;

public class StandardLayout extends Layout {

    public void add(Element element) {
        this.getContainer().add(element);
    }

}
