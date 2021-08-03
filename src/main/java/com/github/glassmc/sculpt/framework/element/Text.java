package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.constraint.Absolute;
import com.github.glassmc.sculpt.framework.constraint.Flexible;
import com.github.glassmc.sculpt.framework.constraint.Constraint;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Text extends Element {

    private Constraint x = new Flexible(), y = new Flexible();
    private Constraint color = new Absolute(new Color(1., 1., 1.));
    private String text = "";
    private Font font = new Font(Font.SERIF, Font.PLAIN, 0);
    private Constraint size = new Flexible();
    private final Map<Direction, Constraint> padding = new HashMap<Direction, Constraint>() {
        {
            for(Direction direction : Direction.values()) {
                put(direction, new Flexible());
            }
        }
    };

    public Text() {
        this.possibleConstructors.add(new Constructor<>());
        this.x.setElement(this);
        this.y.setElement(this);
    }

    @SuppressWarnings("unused")
    public Text x(Constraint x) {
        this.x = x;
        x.setElement(this);
        return this;
    }

    public Constraint getX() {
        return x;
    }

    @SuppressWarnings("unused")
    public Text y(Constraint y) {
        this.y = y;
        y.setElement(this);
        return this;
    }

    public Constraint getY() {
        return y;
    }

    @SuppressWarnings("unused")
    public Text color(Constraint color) {
        this.color = color;
        color.setElement(this);
        return this;
    }

    public Constraint getColor() {
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
    public Text size(Constraint size) {
        this.size = size;
        size.setElement(this);
        return this;
    }

    public Constraint getSize() {
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
    public Text padding(Constraint padding) {
        for(Direction direction : Direction.values()) {
            this.padding.put(direction, padding);
        }
        padding.setElement(this);
        return this;
    }

    @SuppressWarnings("unused")
    public Text padding(Direction direction, Constraint padding) {
        this.padding.put(direction, padding);
        padding.setElement(this);
        return this;
    }

    public Constraint getPadding(Direction direction) {
        return padding.get(direction);
    }

    public static class Constructor<T extends Text> extends Element.Constructor<T> {

        @Override
        public void render(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Text textElement = this.getComponent();
            Color color = textElement.getColor().getConstructor().getColorValue(renderer, appliedElements);

            Font font = textElement.getFont().deriveFont((float) this.getFontSize(renderer, appliedElements));
            Rectangle2D rect = font.getStringBounds(textElement.getText(), new FontRenderContext(null, false, false));

            renderer.getBackend().drawText(textElement.getFont().deriveFont((float) this.getFontSize(renderer, appliedElements)), textElement.getText(), this.getCalculatedX(), this.getCalculatedY(), color);
        }

        @Override
        protected void computePaddings() {
            for(Direction direction : Direction.values()) {
                this.computePaddings(direction, this.getComponent().getPadding(direction));
            }
        }

        @Override
        public double getWidth(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getBounds(renderer, appliedElements).getWidth();
        }

        @Override
        public double getHeight(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            return this.getBounds(renderer, appliedElements).getHeight();
        }

        @Override
        public Constraint getXConstraint() {
            return this.getComponent().getX();
        }

        @Override
        public Constraint getYConstraint() {
            return this.getComponent().getY();
        }

        protected Rectangle2D getBounds(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            FontRenderContext fakeFontRendererContext = new FontRenderContext(null, false, false);
            Text textElement = this.getComponent();
            double size = this.getFontSize(renderer, appliedElements);

            TextLayout textLayout = new TextLayout(textElement.getText(), textElement.getFont().deriveFont((float) size), fakeFontRendererContext);
            return textLayout.getBounds();
        }

        private double getFontSize(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            Text textElement = this.getComponent();
            return textElement.getSize().getConstructor().getFontSizeValue(renderer, appliedElements);
        }

    }

}
