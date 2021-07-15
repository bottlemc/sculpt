package com.github.glassmc.sculpt.v1_8_9;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.loader.Listener;
import com.github.glassmc.sculpt.framework.backend.IRenderBackend;
import com.github.glassmc.sculpt.v1_8_9.test.SculptTransformer;

public class SculptInitializeListener implements Listener {

    @Override
    public void run() {
        GlassLoader.getInstance().registerInterface(IRenderBackend.class, new RenderBackend());

        if(this.isDevelopmentEnvironment()) {
            GlassLoader.getInstance().registerTransformer(SculptTransformer.class);
        }
    }

    private boolean isDevelopmentEnvironment() {
        return SculptInitializeListener.class.getProtectionDomain().getCodeSource().getLocation() == null;
    }

}
