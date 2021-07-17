package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.constraint.Absolute;
import com.github.glassmc.sculpt.framework.constraint.Flexible;
import com.github.glassmc.sculpt.framework.constraint.Constraint;
import com.github.glassmc.sculpt.framework.constraint.Relative;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Text extends Element {

    private Constraint x = new Flexible(), y = new Flexible();
    private Color color = new Color(1., 1., 1.);
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

    @Override
    public Constraint getX() {
        return x;
    }

    @SuppressWarnings("unused")
    public Text y(Constraint y) {
        this.y = y;
        return this;
    }

    @Override
    public Constraint getY() {
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
        public void render(Renderer renderer, ElementData elementData) {
            Text textElement = this.getElement();
            renderer.getBackend().drawText(textElement.getFont().deriveFont((float) this.getFontSize(new Pair<>(this.getElement(), elementData))), textElement.getText(), elementData.getCalculatedX(), elementData.getCalculatedY(), textElement.getColor());
        }

        @Override
        protected void computePaddings(ElementData elementData) {
            for(Element.Direction direction : Element.Direction.values()) {
                this.computePaddings(direction, elementData, this.getElement().getPadding(direction));
            }
        }

        private double getFontSize(Pair<Element, ElementData> elementData) {
            Text textElement = (Text) elementData.getKey();
            Constraint sizeConstraint = textElement.getSize();

            double size = 0;
            if(sizeConstraint instanceof Absolute) {
                size = ((Absolute) sizeConstraint).getValue();
            } else if(sizeConstraint instanceof Relative) {
                size = elementData.getValue().getParentData().getWidth() * ((Relative) sizeConstraint).getPercent();
            }

            return size;
        }

    }

}
