package com.github.glassmc.sculpt.v1_8_9;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class FontRenderer {

    private final CharData[] charData = new CharData[256];

    private final int texture;

    private final int imgSize = 2048;

    public FontRenderer(Font font) {
        font = font.deriveFont(25f);
        this.texture = this.setupTexture(font, this.charData);
    }

    public void drawString(String text, double x, double y, Color color) {
        float alpha = (color.getRGB() >> 24 & 0xFF) / 255.0F;

        GL11.glPushMatrix();

        GL11.glColor4f(1.0F,  1.0F, 1.0F, 1.0F);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha);

        int size = text.length();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture);

        for (int i = 0; i < size; i++) {
            char character = text.charAt(i);
            drawChar(this.charData, character, x, y);
            x += this.charData[character].width - 8;
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    protected int setupTexture(Font font, CharData[] chars) {
        BufferedImage image = generateFontImage(font, chars);
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) -1);
                buffer.put((byte) -1);
                buffer.put((byte) -1);
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        ((Buffer) buffer).flip();

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        return textureId;
    }

    private BufferedImage generateFontImage(Font font, CharData[] chars) {
        BufferedImage bufferedImage = new BufferedImage(this.imgSize, this.imgSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();

        g.setFont(font);

        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, this.imgSize, this.imgSize);
        g.setColor(Color.WHITE);

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        FontMetrics fontMetrics = g.getFontMetrics();

        int charHeight = 0;
        int positionX = 0;
        int positionY = 0;

        for (int i = 0; i < chars.length; i++) {
            char ch = (char) i;

            CharData charData = new CharData();
            Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);

            charData.width = dimensions.getBounds().width + 8;
            charData.height = dimensions.getBounds().height;

            if (positionX + charData.width >= imgSize) {
                positionX = 0;
                positionY += charHeight + 10;
                charHeight = 0;
            }

            if (charData.height > charHeight) {
                charHeight = charData.height;
            }

            charData.storedX = positionX;
            charData.storedY = positionY;

            chars[i] = charData;

            g.drawString(String.valueOf(ch), positionX, positionY + fontMetrics.getAscent());

            positionX += charData.width + 10;
        }

        return bufferedImage;
    }

    protected void drawChar(CharData[] chars, char c, double x, double y) throws ArrayIndexOutOfBoundsException {
        try {
            drawQuad(x, y, chars[c].width, chars[c].height, chars[c].storedX, chars[c].storedY, chars[c].width, chars[c].height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawQuad(double x, double y, double width, double height, double srcX, double srcY, double srcWidth, double srcHeight) {
        double renderSRCX = srcX / imgSize;
        double renderSRCY = srcY / imgSize;
        double renderSRCWidth = srcWidth / imgSize;
        double renderSRCHeight = srcHeight / imgSize;

        GL11.glBegin(GL11.GL_TRIANGLES);

        GL11.glTexCoord2d(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d(x + width, y);

        GL11.glTexCoord2d(renderSRCX, renderSRCY);
        GL11.glVertex2d(x, y);

        GL11.glTexCoord2d(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x, y + height);

        GL11.glTexCoord2d(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x, y + height);

        GL11.glTexCoord2d(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        GL11.glVertex2d(x + width, y + height);

        GL11.glTexCoord2d(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d(x + width, y);

        GL11.glEnd();
    }

    protected static class CharData {
        public int width;
        public int height;
        public int storedX;
        public int storedY;
    }

}