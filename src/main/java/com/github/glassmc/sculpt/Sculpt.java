package com.github.glassmc.sculpt;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.backend.IRenderBackend;
import com.github.glassmc.sculpt.framework.element.Container;

public class Sculpt {

    private Renderer renderer;

    public void render(Container container) {
        if(this.renderer == null) {
            this.renderer = new Renderer(GlassLoader.getInstance().getInterface(IRenderBackend.class));
        }

        this.renderer.render(container);
    }

}
