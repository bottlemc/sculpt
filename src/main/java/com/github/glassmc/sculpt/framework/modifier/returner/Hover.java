package com.github.glassmc.sculpt.framework.modifier.returner;

import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.Vector2D;

public class Hover extends PercentReturner {

    private final long time;

    public Hover(long time) {
        this.possibleConstructors.add(new Constructor<>());
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public static class Constructor<T extends Hover> extends PercentReturner.Constructor<T> {

        private long timeCache = 0;

        private long previousUpdate;

        private ElementData cachedElementData;

        @Override
        public void update(Renderer renderer, ElementData elementData) {
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
            super.update(renderer, elementData);
        }

        private boolean isHovering(Vector2D mouseLocation, ElementData elementData) {
            return mouseLocation.getFirst() > elementData.getCalculatedX() - elementData.getWidth() / 2 &&
                    mouseLocation.getFirst() < elementData.getCalculatedX() + elementData.getWidth() / 2 &&
                    mouseLocation.getSecond() > elementData.getCalculatedY() - elementData.getHeight() / 2 &&
                    mouseLocation.getSecond() < elementData.getCalculatedY() + elementData.getHeight() / 2;
        }

        @Override
        public double getPercent() {
            return timeCache / (double) this.getComponent().getTime();
        }

    }

}
