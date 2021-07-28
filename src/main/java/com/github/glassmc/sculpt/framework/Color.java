package com.github.glassmc.sculpt.framework;

public class Color {

    public static Color getBetween(double percent, Color color1, Color color2) {
        return new Color((color2.red - color1.red) * percent + color1.red,
                (color2.green - color1.green) * percent + color1.green,
                (color2.blue - color1.blue) * percent + color1.blue,
                (color2.alpha - color1.alpha) * percent + color1.alpha);
    }

    private final double red, green, blue, alpha;

    public Color() {
        this(1.0, 1.0, 1.0, 1.0);
    }

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
