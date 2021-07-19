package com.github.glassmc.sculpt.v1_8_9;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Vector2D;
import com.github.glassmc.sculpt.framework.backend.IBackend;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.Window;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public class Backend implements IBackend {

    private final Map<String, FontRenderer> fontCache = new HashMap<>();

    @Override
    public Vector2D getDimension() {
        Window window = new Window(MinecraftClient.getInstance());
        return new Vector2D(window.getScaledWidth(), window.getScaledHeight());
    }

    @Override
    public void preRender() {

    }

    @Override
    public void drawRectangle(double x, double y, double width, double height, Color colorIn) {
        java.awt.Color color = new java.awt.Color((int) (colorIn.getRed() * 255), (int) (colorIn.getGreen() * 255), (int) (colorIn.getBlue() * 255), (int) (colorIn.getAlpha() * 255));
        double xPosition = x - width / 2;
        double yPosition = y - height / 2;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        GlStateManager.color4f(
                color.getRed() / 255.0F,
                color.getGreen() / 255.0F,
                color.getBlue() / 255.0F,
                color.getAlpha() / 255.0F);
        bufferBuilder.begin(7, VertexFormats.POSITION);
        bufferBuilder.vertex(xPosition, yPosition + height, 0.0D).next();
        bufferBuilder.vertex(xPosition + width, yPosition + height, 0.0D).next();
        bufferBuilder.vertex(xPosition + width, yPosition, 0.0D).next();
        bufferBuilder.vertex(xPosition, yPosition, 0.0D).next();
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
        GlStateManager.bindTexture(0);
        GlStateManager.color4f(1f, 1f, 1f, 1f);
    }

    @Override
    public void drawText(Font font, String text, double x, double y, Color color) {
        float baseFontSize = 100;

        String fontName = font.getFontName();
        FontRenderer fontRenderer = fontCache.get(fontName);
        if(fontRenderer == null) {
            fontRenderer = new FontRenderer(font.deriveFont(baseFontSize));
            fontCache.put(fontName, fontRenderer);
        }

        double scale = font.getSize() / baseFontSize * 4;

        TextLayout textLayout = new TextLayout(text, font, new FontRenderContext(null, false, false));
        Rectangle2D bounds = textLayout.getBounds();
        double scaledWidth = bounds.getWidth();
        double scaledHeight = bounds.getHeight();

        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glTranslated(x - scaledWidth / 2, y - scaledHeight / 2, 0);
        GL11.glScaled(scale, scale, 1);

        fontRenderer.drawString(text, 0, 0, new java.awt.Color((int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255), (int) (color.getAlpha() * 255)));

        GL11.glPopMatrix();
    }

    @Override
    public void postRender() {

    }

    @Override
    public Vector2D getMouseLocation() {
        Window window = new Window(MinecraftClient.getInstance());
        double mouseX = (double) Mouse.getX() / Display.getWidth() * window.getScaledWidth();
        double mouseY = window.getScaledHeight() - ((double) Mouse.getY() / Display.getHeight() * window.getScaledHeight());
        return new Vector2D(mouseX, mouseY);
    }

}
