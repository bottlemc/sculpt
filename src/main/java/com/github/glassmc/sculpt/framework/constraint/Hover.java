package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.Vector2D;

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

    public long getTime() {
        return time;
    }

    public Object[] getData() {
        return data;
    }

    public static class Constructor<T extends Hover> extends Constraint.Constructor<T> {

        private long timeCache = 0;

        private long previousUpdate;

        private ElementData cachedElementData;

        protected void update(Renderer renderer, ElementData elementData) {
            long previousUpdateDelta = System.currentTimeMillis() - previousUpdate;

            ElementData effectiveElementData = elementData;

            if(previousUpdate != 0) {
                if(effectiveElementData.getWidth() == 1 && effectiveElementData.getHeight() == 1) {
                    effectiveElementData = cachedElementData;
                }
                if(this.isHovering(renderer.getBackend().getMouseLocation(), effectiveElementData)) {
                    timeCache += previousUpdateDelta;
                } else {
                    timeCache -= previousUpdateDelta;
                }
            }

            timeCache = Math.max(0, Math.min(this.getComponent().getTime(), timeCache));

            previousUpdate = System.currentTimeMillis();

            this.cachedElementData = elementData;
        }

        @Override
        public Color getColorValue(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            this.update(renderer, elementData);

            double percent = (double) timeCache / this.getComponent().getTime();
            return Color.getBetween(percent, (Color) this.getComponent().getData()[0], (Color) this.getComponent().getData()[1]);
        }

        @Override
        public double getWidthValue(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            this.update(renderer, elementData);
            Object[] data = this.getComponent().getData();
            double initialWidth = ((Constraint) data[0]).getConstructor().getWidthValue(renderer, elementData, appliedElements);
            double finalWidth = ((Constraint) data[1]).getConstructor().getWidthValue(renderer, elementData, appliedElements);
            double percent = (double) timeCache / this.getComponent().getTime();
            return (finalWidth - initialWidth) * percent + initialWidth;
        }

        @Override
        public double getHeightValue(Renderer renderer, ElementData elementData, List<ElementData> appliedElements) {
            this.update(renderer, elementData);
            Object[] data = this.getComponent().getData();
            double initialHeight = ((Constraint) data[0]).getConstructor().getHeightValue(renderer, elementData, appliedElements);
            double finalHeight = ((Constraint) data[1]).getConstructor().getHeightValue(renderer, elementData, appliedElements);
            double percent = (double) timeCache / this.getComponent().getTime();
            return (finalHeight - initialHeight) * percent + initialHeight;
        }

        private boolean isHovering(Vector2D mouseLocation, ElementData elementData) {
            return mouseLocation.getFirst() > elementData.getCalculatedX() - elementData.getWidth() / 2 &&
                    mouseLocation.getFirst() < elementData.getCalculatedX() + elementData.getWidth() / 2 &&
                    mouseLocation.getSecond() > elementData.getCalculatedY() - elementData.getHeight() / 2 &&
                    mouseLocation.getSecond() < elementData.getCalculatedY() + elementData.getHeight() / 2;
        }

    }

}
