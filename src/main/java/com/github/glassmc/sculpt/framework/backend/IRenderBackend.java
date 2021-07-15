package com.github.glassmc.sculpt.framework.backend;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Vector2D;

import java.awt.*;

public interface IRenderBackend {

    Vector2D getDimension();

    void preRender();

    void drawRectangle(double x, double y, double width, double height, Color color);

    void drawText(Font font, String text, double x, double y, Color color);

    void postRender();

}
