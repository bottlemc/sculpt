package com.github.glassmc.sculpt;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.loader.Listener;

public class SculptInitializeListener implements Listener {

    @Override
    public void run() {
        GlassLoader.getInstance().registerAPI(new Sculpt());
    }

}
