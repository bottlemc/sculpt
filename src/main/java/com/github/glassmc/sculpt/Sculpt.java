package com.github.glassmc.sculpt;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.Vector2D;
import com.github.glassmc.sculpt.framework.backend.IBackend;
import com.github.glassmc.sculpt.framework.element.Container;

public class Sculpt {

    private final IBackend backend;
    private final Renderer renderer;

    public Sculpt() {
        this.renderer = new Renderer(this.backend = GlassLoader.getInstance().getInterface(IBackend.class));
    }

    public void render(Container container) {
        this.renderer.render(container);
    }

    public IBackend getBackend() {
        return backend;
    }

}
