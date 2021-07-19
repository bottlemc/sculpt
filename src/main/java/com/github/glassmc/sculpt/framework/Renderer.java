package com.github.glassmc.sculpt.framework;

import com.github.glassmc.sculpt.framework.backend.IBackend;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.ArrayList;

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

        Element.Constructor<?> constructor = container.getConstructor();
        constructor.setX(width / 2);
        constructor.setY(height / 2);
        constructor.setWidth(width);
        constructor.setHeight(height);
        container.getConstructor().render(this, new ArrayList<>());

        this.backend.postRender();
    }

    public IBackend getBackend() {
        return backend;
    }

}
