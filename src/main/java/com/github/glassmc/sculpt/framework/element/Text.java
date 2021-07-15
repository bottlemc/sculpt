package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.constraint.Flexible;
import com.github.glassmc.sculpt.framework.constraint.IConstraint;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Text extends Element {

    private IConstraint x = new Flexible(), y = new Flexible();
    private Color color = new Color(1., 1., 1.);
    private String text = "";
    private Font font = new Font(Font.SERIF, Font.PLAIN, 0);
    private IConstraint size = new Flexible();
    private final Map<Direction, IConstraint> padding = new HashMap<Direction, IConstraint>() {
        {
            for(Direction direction : Direction.values()) {
                put(direction, new Flexible());
            }
        }
    };

    @SuppressWarnings("unused")
    public Text x(IConstraint x) {
        this.x = x;
        return this;
    }

    @Override
    public IConstraint getX() {
        return x;
    }

    @SuppressWarnings("unused")
    public Text y(IConstraint y) {
        this.y = y;
        return this;
    }

    @Override
    public IConstraint getY() {
        return y;
    }

    @SuppressWarnings("unused")
    public Text color(Color color) {
        this.color = color;
        return this;
    }

    public Color getColor() {
        return color;
    }

    @SuppressWarnings("unused")
    public Text text(String text) {
        this.text = text;
        return this;
    }

    public String getText() {
        return text;
    }

    @SuppressWarnings("unused")
    public Text size(IConstraint size) {
        this.size = size;
        return this;
    }

    public IConstraint getSize() {
        return size;
    }

    @SuppressWarnings("unused")
    public Text font(Font font) {
        this.font = font;
        return this;
    }

    public Font getFont() {
        return font;
    }

    @SuppressWarnings("unused")
    public Text padding(IConstraint padding) {
        for(Direction direction : Direction.values()) {
            this.padding.put(direction, padding);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public Text padding(Direction direction, IConstraint padding) {
        this.padding.put(direction, padding);
        return this;
    }

    @Override
    public IConstraint getPadding(Direction direction) {
        return padding.get(direction);
    }

}
