package com.github.glassmc.sculpt.v1_8_9.test;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.Sculpt;
import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.element.Container;

public class Hook {

    public static void onRender() {
        Container container = new Container()
                .backgroundEnabled(true)
                .backgroundColor(new Color(1., 0.5, 0.5));

        GlassLoader.getInstance().getAPI(Sculpt.class).render(container);
    }

}
