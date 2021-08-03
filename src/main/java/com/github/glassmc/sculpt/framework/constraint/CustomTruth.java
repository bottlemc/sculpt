package com.github.glassmc.sculpt.framework.constraint;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.List;
import java.util.function.Supplier;

public class CustomTruth extends Constraint {

    private final long time;
    private final Supplier<Boolean> supplier;
    private final Object[] data;

    public CustomTruth(long time, Supplier<Boolean> supplier, Object[] data) {
        this.possibleConstructors.add(new Constructor<>());
        this.time = time;
        this.supplier = supplier;
        this.data = data;
    }

    public CustomTruth(long time, Supplier<Boolean> supplier, Color initialColor, Color finalColor) {
        this(time, supplier, new Object[] {initialColor, finalColor});
    }

    public CustomTruth(long time, Supplier<Boolean> supplier, Constraint initialValue, Constraint finalValue) {
        this(time, supplier, new Object[] {initialValue, finalValue});
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

    public Supplier<Boolean> getSupplier() {
        return supplier;
    }

    public Object[] getData() {
        return data;
    }

    public static class Constructor<T extends CustomTruth> extends Constraint.Constructor<T> {

        private long timeCache = 0;

        private long previousUpdate;

        protected void update() {
            long previousUpdateDelta = System.currentTimeMillis() - previousUpdate;

            if(previousUpdate != 0) {
                if (this.getComponent().getSupplier().get()) {
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
            this.update();
            double percent = (double) timeCache / this.getComponent().getTime();
            return Color.getBetween(percent, (Color) this.getComponent().getData()[0], (Color) this.getComponent().getData()[1]);
        }

        @Override
        public double getXValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            this.update();
            Object[] data = this.getComponent().getData();
            double initialX = ((Constraint) data[0]).getConstructor().getXValue(renderer, appliedElements);
            double finalX = ((Constraint) data[1]).getConstructor().getXValue(renderer, appliedElements);
            double percent = (double) timeCache / this.getComponent().getTime();
            return (finalX - initialX) * percent + initialX;
        }

        @Override
        public double getYValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            this.update();
            Object[] data = this.getComponent().getData();
            double initialY = ((Constraint) data[0]).getConstructor().getYValue(renderer, appliedElements);
            double finalY = ((Constraint) data[1]).getConstructor().getYValue(renderer, appliedElements);
            double percent = (double) timeCache / this.getComponent().getTime();
            return (finalY - initialY) * percent + initialY;
        }

        @Override
        public double getWidthValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            this.update();
            Object[] data = this.getComponent().getData();
            double initialWidth = ((Constraint) data[0]).getConstructor().getWidthValue(renderer, appliedElements);
            double finalWidth = ((Constraint) data[1]).getConstructor().getWidthValue(renderer, appliedElements);
            double percent = (double) timeCache / this.getComponent().getTime();
            return (finalWidth - initialWidth) * percent + initialWidth;
        }

        @Override
        public double getHeightValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            this.update();
            Object[] data = this.getComponent().getData();
            double initialHeight = ((Constraint) data[0]).getConstructor().getHeightValue(renderer, appliedElements);
            double finalHeight = ((Constraint) data[1]).getConstructor().getHeightValue(renderer, appliedElements);
            double percent = (double) timeCache / this.getComponent().getTime();
            return (finalHeight - initialHeight) * percent + initialHeight;
        }

    }

}
