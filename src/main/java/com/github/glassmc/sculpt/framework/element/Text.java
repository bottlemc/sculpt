package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.ElementData;
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
    }

    @SuppressWarnings("unused")
    public Text x(Constraint x) {
        this.x = x;
        return this;
    }

    public Constraint getX() {
        return x;
    }

    @SuppressWarnings("unused")
    public Text y(Constraint y) {
        this.y = y;
        return this;
    }

    public Constraint getY() {
        return y;
    }

    @SuppressWarnings("unused")
    public Text color(Constraint color) {
        this.color = color;
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
        return this;
    }

    @SuppressWarnings("unused")
    public Text padding(Direction direction, Constraint padding) {
        this.padding.put(direction, padding);
        return this;
    }

    public Constraint getPadding(Direction direction) {
        return padding.get(direction);
    }

    public static class Constructor<T extends Text> extends Element.Constructor<T> {

        @Override
        public void render(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            Text textElement = this.getComponent();
            Color color = textElement.getColor().getConstructor().getColorValue(renderer, elementData, appliedElements);
            renderer.getBackend().drawText(textElement.getFont().deriveFont((float) this.getFontSize(elementData, appliedElements)), textElement.getText(), elementData.getCalculatedX(), elementData.getCalculatedY(), color);
        }

        @Override
        protected void computePaddings(ElementData elementData) {
            for(Element.Direction direction : Element.Direction.values()) {
                this.computePaddings(direction, elementData, this.getComponent().getPadding(direction));
            }
        }

        @Override
        public double getWidth(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            return this.getBounds(elementData, appliedElements).getWidth();
        }

        @Override
        public double getHeight(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            return this.getBounds(elementData, appliedElements).getHeight();
        }

        @Override
        public Constraint getXConstraint(ElementData elementData) {
            return this.getComponent().getX();
        }

        @Override
        public Constraint getYConstraint(ElementData elementData) {
            return this.getComponent().getY();
        }

        protected Rectangle2D getBounds(ElementData elementData, List<ElementData> appliedElements) {
            FontRenderContext fakeFontRendererContext = new FontRenderContext(null, false, false);
            Text textElement = this.getComponent();
            double size = this.getFontSize(elementData, appliedElements);

            TextLayout textLayout = new TextLayout(textElement.getText(), textElement.getFont().deriveFont((float) size), fakeFontRendererContext);
            return textLayout.getBounds();
        }

        private double getFontSize(ElementData elementData, List<ElementData> appliedElements) {
            Text textElement = this.getComponent();
            return textElement.getSize().getConstructor().getFontSizeValue(elementData, appliedElements);
        }

    }

}
