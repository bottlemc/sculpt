package com.github.glassmc.sculpt.framework.backend;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Vector2D;

import java.awt.*;
import java.util.List;

public interface IBackend {

    Vector2D getLocation();
    Vector2D getDimension();

    void preRender();

    void drawRectangle(double x, double y, double width, double height, double cornerRadius, Color color);

    void drawText(Font font, String text, double x, double y, Color color);

    void postRender();

    Vector2D getMouseLocation();
    boolean isMouseDown(Button button);

    List<Vector2D> getMouseClicks();

}
