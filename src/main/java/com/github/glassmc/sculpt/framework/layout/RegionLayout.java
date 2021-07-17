package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.ElementData;
import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionLayout extends Layout {

    private final Map<Region, List<Element>> regionMap = new HashMap<>();

    public RegionLayout(Constructor<?> constructor) {
        super(constructor);
    }

    public RegionLayout() {
        this(new Constructor<>());
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
        public List<Pair<Element, ElementData>> getStarterElementData(ElementData containerData) {
            Map<RegionLayout.Region, List<Element>> regionMap = this.getLayout().getRegionMap();
            List<Pair<Element, ElementData>> defaultElementData = new ArrayList<>();
            for(Element element : this.getLayout().getContainer().getChildren()) {
                RegionLayout.Region region = null;
                for(RegionLayout.Region tempRegion : regionMap.keySet()) {
                    if(regionMap.get(tempRegion).contains(element)) {
                        region = tempRegion;
                    }
                }

                if(region == null) {
                    continue;
                }

                defaultElementData.add(new Pair<>(element, new ElementData(containerData, region.getX() * containerData.getWidth() / 2, region.getY() * containerData.getHeight() / 2, 1, 1)));
            }

            return defaultElementData;
        }

    }

}
