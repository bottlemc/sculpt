package com.github.glassmc.sculpt.v1_8_9.test;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.KeyAction;
import com.github.glassmc.sculpt.Sculpt;
import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.MouseAction;
import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.Vector2D;
import com.github.glassmc.sculpt.framework.backend.IBackend;
import com.github.glassmc.sculpt.framework.backend.Key;
import com.github.glassmc.sculpt.framework.constraint.*;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.element.Element;
import com.github.glassmc.sculpt.framework.element.Text;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

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

            /*container = new Container()
                    .backgroundColor(new Absolute(new Color(0.2, 0.2, 0.2)))
                    .getLayout(RegionLayout.class)
                    .add(new Container()
                            .backgroundColor(new Absolute(new Color(0.15, 0.15, 0.15)))
                            .width(new Relative(0.75, true))
                            .height(new Relative(0.9))
                            .layout(new ListLayout(ListLayout.Type.VERTICAL))
                            .apply(container1 -> {
                                for(String category : servers.keySet()) {
                                    container1.getLayout(ListLayout.class)
                                            .add(new Container()
                                                    .height(new Relative(0.05))
                                                    .padding(new Relative(0.0125))
                                                    .getLayout(RegionLayout.class)
                                                    .add(new Text()
                                                            .size(new Relative(0.05))
                                                            .font(roboto)
                                                            .text(category)
                                                            .color(new Absolute(new Color(0.8, 0.8, 0.8))),
                                                            RegionLayout.Region.LEFT)
                                                    .add(new Container()
                                                            .backgroundColor(new Absolute(new Color(0.8, 0.8, 0.8)))
                                                            .height(new Absolute(1))
                                                            .padding(Element.Direction.LEFT, new Relative(0.05))
                                                            .padding(Element.Direction.RIGHT, new Relative(0.05)),
                                                            RegionLayout.Region.RIGHT)
                                                    .getContainer());

                                    for(ServerData data : servers.get(category)) {
                                        container1.getLayout(ListLayout.class)
                                                .add(new Container()
                                                        .backgroundColor(new Absolute(new Color(0.2, 0.2, 0.2)))
                                                        .height(new Relative(0.15))
                                                        .padding(new Relative(0.0125))
                                                        .getLayout(RegionLayout.class)
                                                        .add(new Container()
                                                                .backgroundColor(new Absolute(new Color(0.4, 0.4, 0.4)))
                                                                .width(new Copy())
                                                                .padding(new Relative(0.0175)),
                                                                RegionLayout.Region.LEFT)
                                                        .add(new Container()
                                                                        .width(new Copy())
                                                                        .padding(new Relative(0.0175))
                                                                        .getLayout(RegionLayout.class)
                                                                        .add(new Container()
                                                                                .backgroundColor(new Hover(150, new Color(0.25, 0.25, 0.25), new Color(0.5, 0.5, 0.5)))
                                                                                .width(new Hover(150, new Relative(0.3), new Relative(0.8)))
                                                                                .height(new Relative(0.5)),
                                                                                RegionLayout.Region.TOP)
                                                                        .add(new Container()
                                                                                .backgroundColor(new Absolute(new Color(0.25, 0.25, 0.25)))
                                                                                .height(new Relative(0.5)),
                                                                                RegionLayout.Region.BOTTOM)
                                                                        .getContainer(),
                                                                RegionLayout.Region.RIGHT)
                                                        .add(new Container()
                                                                        .getLayout(RegionLayout.class)
                                                                        .add(new Text()
                                                                                .size(new Relative(0.065))
                                                                                .font(roboto)
                                                                                .padding(new Relative(0.025))
                                                                                .color(new Absolute(new Color(0.8, 0.8, 0.8)))
                                                                                .text(data.name)
                                                                                .y(new Side(Side.Direction.NEGATIVE)),
                                                                                RegionLayout.Region.LEFT)
                                                                        .add(new Text()
                                                                                .size(new Relative(0.045))
                                                                                .font(roboto)
                                                                                .padding(new Relative(0.025))
                                                                                .color(new Absolute(new Color(0.5, 0.5, 0.5)))
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
                    .getContainer();*/

            container = new Container()
                    .getLayout(RegionLayout.class)
                    .add(new Container()
                        .width(new Relative(0.5))
                        .height(new Relative(0.5))
                        .getLayout(RegionLayout.class)
                        .add(new Container()
                                .height(new Absolute(30))
                                .backgroundColor(new Absolute(new Color(0., 1., 0.)))
                                .getLayout(RegionLayout.class)
                                .add(new Text()
                                        .text("abcedfghijklmopqrstuvwxyz")
                                        .font(Font.createFont(Font.TRUETYPE_FONT, Hook.class.getClassLoader().getResourceAsStream("Raleway-Bold.ttf")))
                                        .size(new Relative(0.5, 0, true)),
                                        RegionLayout.Region.CENTER)
                                .getContainer(),
                                RegionLayout.Region.TOP)
                        .add(new Container()
                                .backgroundImage("sculpt/desktop.png")
                                .cornerRadius(new Pair<>(Element.Direction.TOP, Element.Direction.RIGHT), new Absolute(3.5))
                                .onClick(interact -> {
                                    System.out.println("Test");
                                })
                                .onPress(interact -> {
                                    System.out.println(interact.getCharacter());
                                }),
                                RegionLayout.Region.BOTTOM)
                        .getContainer(),
                        RegionLayout.Region.CENTER)
                    .getContainer();
        }

        GlassLoader.getInstance().getAPI(Sculpt.class).render(container);
    }

    @SuppressWarnings("unused")
    public static void onAction() {
        if(Mouse.getEventButton() != -1) {
            Window window = new Window(MinecraftClient.getInstance());
            double mouseX = (double) Mouse.getX() / Display.getWidth() * window.getScaledWidth();
            double mouseY = window.getScaledHeight() - ((double) Mouse.getY() / Display.getHeight() * window.getScaledHeight());
            GlassLoader.getInstance().getInterface(IBackend.class).getMouseActions().add(new MouseAction(Mouse.getEventButtonState() ? MouseAction.Type.CLICK : MouseAction.Type.RELEASE, new Vector2D(mouseX, mouseY)));
        }
    }

    @SuppressWarnings("unused")
    public static void onKey() {
        if(Keyboard.getEventKeyState()) {
            GlassLoader.getInstance().getInterface(IBackend.class).getKeyActions().add(new KeyAction(getKey(Keyboard.getEventKey()), Keyboard.getEventCharacter()));
        }
    }

    private static Key getKey(int keycode) {
        switch (keycode) {
            case Keyboard.KEY_ESCAPE:
                return Key.ESCAPE;
        }
        return null;
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
