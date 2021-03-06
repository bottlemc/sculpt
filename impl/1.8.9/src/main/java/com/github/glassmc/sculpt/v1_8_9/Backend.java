package com.github.glassmc.sculpt.v1_8_9;

import com.github.glassmc.sculpt.KeyAction;
import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.MouseAction;
import com.github.glassmc.sculpt.framework.Vector2D;
import com.github.glassmc.sculpt.framework.backend.Button;
import com.github.glassmc.sculpt.framework.backend.IBackend;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Backend implements IBackend {

    private final Map<String, FontRenderer> fontCache = new HashMap<>();

    private final List<MouseAction> mouseActions = new ArrayList<>();
    private final List<KeyAction> keyActions = new ArrayList<>();

    @Override
    public Vector2D getLocation() {
        Vector2D dimension = this.getDimension();
        return new Vector2D(dimension.getFirst() / 2, dimension.getSecond() / 2);
    }

    @Override
    public Vector2D getDimension() {
        Window window = new Window(MinecraftClient.getInstance());
        return new Vector2D(window.getScaledWidth(), window.getScaledHeight());
    }

    @Override
    public void preRender() {

    }

    @Override
    public void drawRectangle(double x, double y, double width, double height, double topLeftCornerRadius, double topRightCornerRadius, double bottomRightCornerRadius, double bottomLeftCornerRadius, Color colorIn) {
        GL11.glColor4f(1, 1, 1, 1);

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
        bufferBuilder.begin(GL11.GL_POLYGON, VertexFormats.POSITION);
        bufferBuilder.vertex(xPosition + bottomLeftCornerRadius, yPosition + height, 0).next();
        bufferBuilder.vertex(xPosition + width - bottomRightCornerRadius, yPosition + height, 0).next();
        for (int i = 0; i < 90; i += 2) {
            bufferBuilder.vertex(xPosition + width - bottomRightCornerRadius + Math.sin(Math.toRadians(i)) * bottomRightCornerRadius, yPosition + height - bottomRightCornerRadius + Math.cos(Math.toRadians(i)) * bottomRightCornerRadius, 0).next();
        }
        bufferBuilder.vertex(xPosition + width, yPosition + height - bottomRightCornerRadius, 0).next();
        bufferBuilder.vertex(xPosition + width, yPosition + topRightCornerRadius, 0).next();
        for (int i = 90; i < 180; i += 2) {
            bufferBuilder.vertex(xPosition + width - topRightCornerRadius + Math.sin(Math.toRadians(i)) * topRightCornerRadius, yPosition + topRightCornerRadius + Math.cos(Math.toRadians(i)) * topRightCornerRadius, 0).next();
        }
        bufferBuilder.vertex(xPosition + width - topRightCornerRadius, yPosition, 0).next();
        bufferBuilder.vertex(xPosition + topLeftCornerRadius, yPosition, 0).next();
        for (int i = 180; i < 270; i += 2) {
            bufferBuilder.vertex(xPosition + topLeftCornerRadius + Math.sin(Math.toRadians(i)) * topLeftCornerRadius, yPosition + topLeftCornerRadius + Math.cos(Math.toRadians(i)) * topLeftCornerRadius, 0).next();
        }
        bufferBuilder.vertex(xPosition, yPosition + topLeftCornerRadius, 0).next();
        bufferBuilder.vertex(xPosition, yPosition + height - bottomLeftCornerRadius, 0).next();
        for (int i = 270; i < 360; i += 2) {
            bufferBuilder.vertex(xPosition + bottomLeftCornerRadius + Math.sin(Math.toRadians(i)) * bottomLeftCornerRadius, yPosition + height - bottomLeftCornerRadius + Math.cos(Math.toRadians(i)) * bottomLeftCornerRadius, 0).next();
        }
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
        GlStateManager.bindTexture(0);
        GlStateManager.color4f(1f, 1f, 1f, 1f);
    }

    @Override
    public void drawImage(double x, double y, double width, double height, String image, Color colorIn) {
        Identifier identifier = new Identifier(image);

        java.awt.Color color = new java.awt.Color((int) (colorIn.getRed() * 255), (int) (colorIn.getGreen() * 255), (int) (colorIn.getBlue() * 255), (int) (colorIn.getAlpha() * 255));
        double xPosition = x - width / 2;
        double yPosition = y - height / 2;

        MinecraftClient.getInstance().getTextureManager().bindTexture(identifier);
        GlStateManager.enableBlend();
        GlStateManager.enableTexture();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        GlStateManager.color4f(
                color.getRed() / 255.0F,
                color.getGreen() / 255.0F,
                color.getBlue() / 255.0F,
                color.getAlpha() / 255.0F);
        bufferBuilder.begin(GL11.GL_POLYGON, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(xPosition, yPosition + height, 0).texture(0, 1).next();
        bufferBuilder.vertex(xPosition + width, yPosition + height, 0).texture(1, 1).next();
        bufferBuilder.vertex(xPosition + width, yPosition, 0).texture(1, 0).next();
        bufferBuilder.vertex(xPosition, yPosition, 0).texture(0, 0).next();
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
        GlStateManager.bindTexture(0);
        GlStateManager.color4f(1f, 1f, 1f, 1f);
    }

    @Override
    public void drawText(Font font, String text, double x, double y, Color color) {
        float baseFontSize = 25;

        String fontName = font.getFontName();
        FontRenderer fontRenderer = fontCache.get(fontName);
        if(fontRenderer == null) {
            fontRenderer = new FontRenderer(font.deriveFont(baseFontSize));
            fontCache.put(fontName, fontRenderer);
        }

        double scale = font.getSize() / baseFontSize;

        TextLayout textLayout = new TextLayout(text, font, new FontRenderContext(null, false, false));
        Canvas canvas = new Canvas();
        FontMetrics fontMetrics = canvas.getFontMetrics(font);
        Rectangle2D bounds = textLayout.getBounds();
        double scaledWidth = bounds.getWidth();
        double scaledHeight = textLayout.getAscent();

        GL11.glPushMatrix();

        GL11.glTranslated(x - scaledWidth / 2, y - scaledHeight / 2, 0);
        GL11.glScaled(scale, scale, 1);

        fontRenderer.drawString(text, 0, 0, new java.awt.Color((int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255), (int) (color.getAlpha() * 255)));

        GL11.glPopMatrix();
    }

    @Override
    public void postRender() {
        this.mouseActions.clear();
        this.keyActions.clear();
    }

    @Override
    public Vector2D getMouseLocation() {
        Window window = new Window(MinecraftClient.getInstance());
        double mouseX = (double) Mouse.getX() / Display.getWidth() * window.getScaledWidth();
        double mouseY = window.getScaledHeight() - ((double) Mouse.getY() / Display.getHeight() * window.getScaledHeight());
        return new Vector2D(mouseX, mouseY);
    }

    @Override
    public boolean isMouseDown(Button button) {
        int buttonId = 0;
        switch (button) {
            case PRIMARY:
                buttonId = 0;
                break;
            case SECONDARY:
                buttonId = 1;
                break;
        }
        return Mouse.isButtonDown(buttonId);
    }

    @Override
    public List<MouseAction> getMouseActions() {
        return mouseActions;
    }

    @Override
    public List<KeyAction> getKeyActions() {
        return keyActions;
    }

}
