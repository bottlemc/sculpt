package com.github.glassmc.sculpt.client.v1_8_9;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.loader.Listener;
import com.github.glassmc.sculpt.framework.backend.IRenderBackend;

public class SculptInitializeListener implements Listener {

    @Override
    public void run() {
        GlassLoader.getInstance().registerInterface(IRenderBackend.class, new RenderBackend());
    }

}
