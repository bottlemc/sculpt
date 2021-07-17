package com.github.glassmc.sculpt.framework;

public class ElementData {

    private final ElementData parentData;
    private double x, y;
    private double width, height;
    private double leftPadding, rightPadding, topPadding, bottomPadding;

    public ElementData(ElementData parentData, double x, double y, double width, double height) {
        this.parentData = parentData;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ElementData getParentData() {
        return parentData;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(double leftPadding) {
        this.leftPadding = leftPadding;
    }

    public double getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(double topPadding) {
        this.topPadding = topPadding;
    }

    public double getRightPadding() {
        return rightPadding;
    }

    public void setRightPadding(double rightPadding) {
        this.rightPadding = rightPadding;
    }

    public double getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(double bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public double getCalculatedX() {
        return (this.parentData != null ? this.parentData.getCalculatedX() : 0) + this.x;
    }

    public double getCalculatedY() {
        return (this.parentData != null ? this.parentData.getCalculatedY() : 0) + this.y;
    }

    public double getLeft() {
        return this.x - this.width / 2;
    }

    public double getRight() {
        return this.x + this.width / 2;
    }

    public double getTop() {
        return this.y - this.height / 2;
    }

    public double getBottom() {
        return this.y + this.height / 2;
    }

}
