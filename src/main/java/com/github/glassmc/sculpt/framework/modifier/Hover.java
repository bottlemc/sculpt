package com.github.glassmc.sculpt.framework.modifier;

import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.Vector2D;

public class Hover extends Modifier {

    private long time;

    public Hover() {
        this.possibleConstructors.add(new Constructor<>());
    }

    public Hover setTime(long time) {
        this.time = time;
        return this;
    }

    public long getTime() {
        return time;
    }

    public static class Constructor<T extends Hover> extends Modifier.Constructor<T> {

        private long timeCache = 0;

        private long previousUpdate;

        @Override
        public void update(Renderer renderer, ElementData elementData) {
            long previousUpdateDelta = System.currentTimeMillis() - previousUpdate;

            if(previousUpdate != 0) {
                if(this.isHovering(renderer.getBackend().getMouseLocation(), elementData)) {
                    timeCache += previousUpdateDelta;
                } else {
                    timeCache -= previousUpdateDelta;
                }
            }

            timeCache = Math.max(0, Math.min(this.getComponent().getTime(), timeCache));

            previousUpdate = System.currentTimeMillis();
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
