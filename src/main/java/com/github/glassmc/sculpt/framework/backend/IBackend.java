package com.github.glassmc.sculpt.framework.backend;

import com.github.glassmc.sculpt.KeyAction;
import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.MouseAction;
import com.github.glassmc.sculpt.framework.Vector2D;

import java.awt.*;
import java.net.URL;
import java.util.List;

public interface IBackend {

    Vector2D getLocation();
    Vector2D getDimension();

    void preRender();

    void drawRectangle(double x, double y, double width, double height, double topLeftCornerRadius, double topRightCornerRadius, double bottomRightCornerRadius, double bottomLeftCornerRadius, Color color);
    void drawImage(double x, double y, double width, double height, String image, Color color);
    void drawText(Font font, String text, double x, double y, Color color);

    void postRender();

    Vector2D getMouseLocation();
    boolean isMouseDown(Button button);

    List<MouseAction> getMouseActions();

    List<KeyAction> getKeyActions();
}
