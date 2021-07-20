package com.github.glassmc.sculpt.framework.layout;

import com.github.glassmc.sculpt.framework.element.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StageLayout extends Layout {

    private final Map<String, List<Element>> stageMap = new HashMap<>();
    private String currentStage;

    public StageLayout() {
        this.possibleConstructors.add(new Constructor<>());
    }

    public StageLayout add(String section, Element element) {
        this.getContainer().add(element);
        this.stageMap.computeIfAbsent(section, k -> new ArrayList<>()).add(element);
        return this;
    }

    public StageLayout setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
        return this;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public Map<String, List<Element>> getElementMap() {
        return stageMap;
    }

    public static class Constructor<T extends StageLayout> extends Layout.Constructor<T> {

        @Override
        public List<Element.Constructor<?>> getDefaultElements() {
            Map<String, List<Element>> elementMap = this.getComponent().getElementMap();
            List<Element.Constructor<?>> defaultElements = new ArrayList<>();
            for(Element element : this.getComponent().getContainer().getChildren()) {
                String area = null;
                for(String tempArea : elementMap.keySet()) {
                    if(elementMap.get(tempArea).contains(element)) {
                        area = tempArea;
                    }
                }

                if(area == null || !area.equals(this.getComponent().getCurrentStage())) {
                    continue;
                }

                Element.Constructor<?> elementConstructor = element.getConstructor();
                defaultElements.add(elementConstructor);
            }

            return defaultElements;
        }

    }

}
