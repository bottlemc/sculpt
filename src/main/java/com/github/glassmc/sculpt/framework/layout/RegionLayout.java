package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.element.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionLayout extends Layout {

    private final Map<Region, List<Element>> regionMap = new HashMap<>();

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

}
