package com.github.glassmc.sculpt.v1_8_9.test;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.Sculpt;
import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.constraint.*;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.element.Element;
import com.github.glassmc.sculpt.framework.element.Text;
import com.github.glassmc.sculpt.framework.layout.ListLayout;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;
import com.github.glassmc.sculpt.framework.modifier.Hover;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Hook {

    private static Container container = null;

    @SuppressWarnings("unused")
    public static void onRender() throws IOException, FontFormatException {
        Map<String, List<ServerData>> servers = new HashMap<>();
        servers.put("Test Category", Collections.singletonList(new ServerData("Test Server", "Test Description")));

        if(container == null) {
            Font roboto = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Hook.class.getClassLoader().getResourceAsStream("Roboto-Regular.ttf")));

            container = new Container()
                    .backgroundEnabled(true)
                    .backgroundColor(new Color(0.2, 0.2, 0.2))
                    .getLayout(RegionLayout.class)
                    .add(new Container()
                            .backgroundEnabled(true)
                            .backgroundColor(new Color(0.15, 0.15, 0.15))
                            .width(new Relative(0.75, true))
                            .height(new Relative(0.9))
                            .layout(new ListLayout(ListLayout.Type.VERTICAL))
                            .apply(container1 -> {
                                for(String category : servers.keySet()) {
                                    container1.getLayout(ListLayout.class)
                                            .add(new Container()
                                                    .width(new Flexible())
                                                    .height(new Relative(0.05))
                                                    .padding(new Relative(0.0125))
                                                    .getLayout(RegionLayout.class)
                                                    .add(new Text()
                                                            .size(new Relative(0.05))
                                                            .font(roboto)
                                                            .text(category)
                                                            .color(new Color(0.8, 0.8, 0.8)),
                                                            RegionLayout.Region.LEFT)
                                                    .add(new Container()
                                                            .backgroundEnabled(true)
                                                            .backgroundColor(new Color(0.8, 0.8, 0.8))
                                                            .width(new Flexible())
                                                            .height(new Absolute(1))
                                                            .padding(Element.Direction.LEFT, new Relative(0.05))
                                                            .padding(Element.Direction.RIGHT, new Relative(0.05)),
                                                            RegionLayout.Region.RIGHT)
                                                    .getContainer());

                                    for(ServerData data : servers.get(category)) {
                                        container1.getLayout(ListLayout.class)
                                                .add(new Container()
                                                        .backgroundEnabled(true)
                                                        .backgroundColor(new Color(0.2, 0.2, 0.2))
                                                        .width(new Flexible())
                                                        .height(new Relative(0.15))
                                                        .padding(new Relative(0.0125))
                                                        .getLayout(RegionLayout.class)
                                                        .add(new Container()
                                                                .backgroundEnabled(true)
                                                                .backgroundColor(new Color(0.4, 0.4, 0.4))
                                                                .width(new Copy())
                                                                .height(new Flexible())
                                                                .padding(new Relative(0.0175)),
                                                                RegionLayout.Region.LEFT)
                                                        .add(new Container()
                                                                        .backgroundEnabled(true)
                                                                        .width(new Copy())
                                                                        .height(new Flexible())
                                                                        .padding(new Relative(0.0175))
                                                                        .getLayout(RegionLayout.class)
                                                                        .add(new Container()
                                                                                .backgroundEnabled(true)
                                                                                .backgroundColor(new Hover().setTime(500), new Color(0.25, 0.25, 0.25), new Color(0.5, 0.5, 0.5))
                                                                                .width(new Flexible())
                                                                                .height(new Relative(0.5)),
                                                                                RegionLayout.Region.TOP)
                                                                        .add(new Container()
                                                                                .backgroundEnabled(true)
                                                                                .backgroundColor(new Color(0.25, 0.25, 0.25))
                                                                                .width(new Flexible())
                                                                                .height(new Relative(0.5)),
                                                                                RegionLayout.Region.BOTTOM)
                                                                        .getContainer(),
                                                                RegionLayout.Region.RIGHT)
                                                        .add(new Container()
                                                                        .width(new Flexible())
                                                                        .height(new Flexible())
                                                                        .getLayout(RegionLayout.class)
                                                                        .add(new Text()
                                                                                .size(new Relative(0.065))
                                                                                .font(roboto)
                                                                                .padding(new Relative(0.025))
                                                                                .color(new Color(0.8, 0.8, 0.8))
                                                                                .text(data.name)
                                                                                .y(new Side(Side.Direction.NEGATIVE)),
                                                                                RegionLayout.Region.LEFT)
                                                                        .add(new Text()
                                                                                .size(new Relative(0.045))
                                                                                .font(roboto)
                                                                                .padding(new Relative(0.025))
                                                                                .color(new Color(0.5, 0.5, 0.5))
                                                                                .text(data.description)
                                                                                .y(new Side(Side.Direction.NEGATIVE)),
                                                                                RegionLayout.Region.LEFT)
                                                                        .getContainer(),
                                                                RegionLayout.Region.CENTER)
                                                        .getContainer());
                                    }
                                }
                            }),
                            RegionLayout.Region.CENTER)
                    .getContainer();
        }

        GlassLoader.getInstance().getAPI(Sculpt.class).render(container);
    }

    private static class ServerData {

        private final String name;
        private final String description;

        public ServerData(String name, String description) {
            this.name = name;
            this.description = description;
        }

    }

}
