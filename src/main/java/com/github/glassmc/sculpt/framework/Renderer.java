package com.github.glassmc.sculpt.framework;

import com.github.glassmc.sculpt.framework.backend.IBackend;
import com.github.glassmc.sculpt.framework.element.Container;

public class Renderer {

    private final IBackend backend;

    public Renderer(IBackend backend) {
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

    public IBackend getBackend() {
        return backend;
    }

}
