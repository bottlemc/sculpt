package com.github.glassmc.sculpt.framework;

import com.github.glassmc.sculpt.framework.backend.IRenderBackend;
import com.github.glassmc.sculpt.framework.element.Container;

public class Renderer {

    private final IRenderBackend backend;

    public Renderer(IRenderBackend backend) {
        this.backend = backend;
    }

    public void render(Container container) {
        this.backend.preRender();

        Vector2D dimension = this.backend.getDimension();
        double width = dimension.getFirst();
        double height = dimension.getSecond();

        ElementData fakeParent = new ElementData(null, 0, 0, 1, 1);
        ElementData elementData = new ElementData(fakeParent, width / 2, height / 2, width, height);
        container.getConstructor().render(this, elementData);

        this.backend.postRender();
    }

    public IRenderBackend getBackend() {
        return backend;
    }

}
