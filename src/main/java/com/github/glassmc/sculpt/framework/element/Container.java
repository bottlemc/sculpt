package com.github.glassmc.sculpt.framework.element;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.constraint.Flexible;
import com.github.glassmc.sculpt.framework.constraint.IConstraint;
import com.github.glassmc.sculpt.framework.layout.Layout;
import com.github.glassmc.sculpt.framework.layout.StandardLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container extends Element {

    private Layout layout = new StandardLayout();

    private IConstraint x = new Flexible(), y = new Flexible();
    private boolean backgroundEnabled = false;
    private Color backgroundColor = new Color(1., 1., 1.);
    private IConstraint width = new Flexible(), height = new Flexible();
    private final Map<Direction, IConstraint> padding = new HashMap<Direction, IConstraint>() {
        {
            for(Direction direction : Direction.values()) {
                put(direction, new Flexible());
            }
        }
    };
    private final List<Element> children = new ArrayList<>();

    @SuppressWarnings("unused")
    public Container x(IConstraint x) {
        this.x = x;
        return this;
    }

    @Override
    public IConstraint getX() {
        return x;
    }

    @SuppressWarnings("unused")
    public Container y(IConstraint y) {
        this.y = y;
        return this;
    }

    @Override
    public IConstraint getY() {
        return y;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Container add(Element element) {
        this.children.add(element);
        element.setParent(this);
        return this;
    }

    public List<Element> getChildren() {
        return this.children;
    }

    @SuppressWarnings("unused")
    public Container width(IConstraint width) {
        this.width = width;
        return this;
    }

    public IConstraint getWidth() {
        return width;
    }

    @SuppressWarnings("unused")
    public Container height(IConstraint height) {
        this.height = height;
        return this;
    }

    public IConstraint getHeight() {
        return height;
    }

    @SuppressWarnings("unused")
    public Container backgroundEnabled(boolean backgroundEnabled) {
        this.backgroundEnabled = backgroundEnabled;
        return this;
    }

    public boolean isBackgroundEnabled() {
        return backgroundEnabled;
    }

    @SuppressWarnings("unused")
    public Container backgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @SuppressWarnings("unused")
    public Container padding(IConstraint padding) {
        for(Direction direction : Direction.values()) {
            this.padding.put(direction, padding);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public Container padding(Direction direction, IConstraint padding) {
        this.padding.put(direction, padding);
        return this;
    }

    @Override
    public IConstraint getPadding(Direction direction) {
        return padding.get(direction);
    }

    @SuppressWarnings("unused")
    public Container layout(Layout layout) {
        this.layout = layout;
        layout.setContainer(this);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Layout> T getLayout() {
        return (T) this.layout;
    }

    @SuppressWarnings("unused")
    public <T extends Layout> T getLayout(Class<T> layoutClass) {
        return layoutClass.cast(this.layout);
    }

}
