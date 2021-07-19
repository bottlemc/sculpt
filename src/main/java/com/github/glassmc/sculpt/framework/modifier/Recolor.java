package com.github.glassmc.sculpt.framework.modifier;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.modifier.returner.PercentReturner;

public class Recolor extends Modifier {

    private final PercentReturner returner;
    private final Color initialColor, finalColor;

    public Recolor(PercentReturner returner, Color initialColor, Color finalColor) {
        this.possibleConstructors.add(new Constructor<>());
        this.returner = returner;
        this.initialColor = initialColor;
        this.finalColor = finalColor;
    }

    public PercentReturner getReturner() {
        return returner;
    }

    public Color getInitialColor() {
        return initialColor;
    }

    public Color getFinalColor() {
        return finalColor;
    }

    public static class Constructor<T extends Recolor> extends Modifier.Constructor<T> {

        @Override
        public Color getColor(Renderer renderer, ElementData elementData) {
            this.getComponent().getReturner().getConstructor().update(renderer, elementData);
            return Color.getBetween(this.getComponent().getReturner().getConstructor().getPercent(), this.getComponent().getInitialColor(), this.getComponent().getFinalColor());
        }

    }

}
