package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.Vector2D;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.List;

public class Hover extends Constraint {

    private final long time;
    private final Object[] data;

    public Hover(long time, Object[] data) {
        this.possibleConstructors.add(new Constructor<>());
        this.time = time;
        this.data = data;
    }

    public Hover(long time, Color initialColor, Color finalColor) {
        this(time, new Object[] {initialColor, finalColor});
    }

    public Hover(long time, Constraint initialValue, Constraint finalValue) {
        this(time, new Object[] {initialValue, finalValue});
    }

    @Override
    public void setElement(Element element) {
        if(this.data[0] instanceof Constraint) {
            ((Constraint) this.data[0]).setElement(element);
            ((Constraint) this.data[1]).setElement(element);
        }
        super.setElement(element);
    }

    public long getTime() {
        return time;
    }

    public Object[] getData() {
        return data;
    }

    public static class Constructor<T extends Hover> extends Constraint.Constructor<T> {

        private long timeCache = 0;

        private long previousUpdate;

        protected void update(Renderer renderer) {
            long previousUpdateDelta = System.currentTimeMillis() - previousUpdate;

            if(previousUpdate != 0) {
                if(this.isOnTop(renderer.getBackend().getMouseLocation(), this.getComponent().getElement().getConstructor())) {
                    timeCache += previousUpdateDelta;
                } else {
                    timeCache -= previousUpdateDelta;
                }
            }

            timeCache = Math.max(0, Math.min(this.getComponent().getTime(), timeCache));

            previousUpdate = System.currentTimeMillis();
        }

        @Override
        public Color getColorValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            this.update(renderer);

            double percent = (double) timeCache / this.getComponent().getTime();
            return Color.getBetween(percent, (Color) this.getComponent().getData()[0], (Color) this.getComponent().getData()[1]);
        }

        @Override
        public double getWidthValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            this.update(renderer);
            Object[] data = this.getComponent().getData();
            double initialWidth = ((Constraint) data[0]).getConstructor().getWidthValue(renderer, appliedElements);
            double finalWidth = ((Constraint) data[1]).getConstructor().getWidthValue(renderer, appliedElements);
            double percent = (double) timeCache / this.getComponent().getTime();
            return (finalWidth - initialWidth) * percent + initialWidth;
        }

        @Override
        public double getHeightValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            this.update(renderer);
            Object[] data = this.getComponent().getData();
            double initialHeight = ((Constraint) data[0]).getConstructor().getHeightValue(renderer, appliedElements);
            double finalHeight = ((Constraint) data[1]).getConstructor().getHeightValue(renderer, appliedElements);
            double percent = (double) timeCache / this.getComponent().getTime();
            return (finalHeight - initialHeight) * percent + initialHeight;
        }

    }

}
