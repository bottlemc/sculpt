package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.element.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionLayout extends Layout {

    private final Map<Region, List<Element>> regionMap = new HashMap<>();

    public RegionLayout() {
        this.possibleConstructors.add(new Constructor<>());
    }

    public RegionLayout add(Element element, Region region) {
        this.getContainer().add(element);
        this.regionMap.computeIfAbsent(region, k -> new ArrayList<>()).add(element);
        return this;
    }

    public Map<Region, List<Element>> getRegionMap() {
        return regionMap;
    }

    public enum Region {
        LEFT(-1, 0),
        RIGHT(1, 0),
        TOP(0, -1),
        BOTTOM(0, 1),
        CENTER(0, 0);

        private final int x, y;

        Region(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

    }

    public static class Constructor<T extends RegionLayout> extends Layout.Constructor<T> {

        @Override
        public List<Element.Constructor<?>> getDefaultElements() {
            Map<Region, List<Element>> regionMap = this.getComponent().getRegionMap();
            List<Element.Constructor<?>> defaultElements = new ArrayList<>();
            for(Element element : this.getComponent().getContainer().getChildren()) {
                Region region = null;
                for(Region tempRegion : regionMap.keySet()) {
                    if(regionMap.get(tempRegion).contains(element)) {
                        region = tempRegion;
                    }
                }

                if(region == null) {
                    continue;
                }

                Element.Constructor<?> containerConstructor = this.getComponent().getContainer().getConstructor();

                Element.Constructor<?> elementConstructor = element.getConstructor();
                elementConstructor.setX(region.getX() * containerConstructor.getWidth() / 2);
                elementConstructor.setY(region.getY() * containerConstructor.getHeight() / 2);

                defaultElements.add(elementConstructor);
            }

            return defaultElements;
        }

    }

}
