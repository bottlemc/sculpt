package com.github.glassmc.sculpt.framework;

public class Color {

    private final double red, green, blue, alpha;

    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public Color(int red, int green, int blue, int alpha) {
        this(red / 255., green / 255., blue / 255., alpha / 255.);
    }

    public Color(double red, double green, double blue, double alpha) {
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.alpha = alpha;
    }

    public Color(double red, double green, double blue) {
        this(red, green, blue, 1);
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    public double getAlpha() {
        return alpha;
    }

    @Override
    public String toString() {
        return "Color{" + "red=" + red + ", green=" + green + ", blue=" + blue + ", alpha=" + alpha + "}";
    }
}
