package com.nukedemo;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTransformations {

    public static BufferedImage concatenateImagesHorizontally(BufferedImage img1, BufferedImage img2) {
        // Get the width and height of the resulting image
        int width = img1.getWidth() + img2.getWidth();
        int height = Math.max(img1.getHeight(), img2.getHeight());

        // Create a new image with the desired dimensions
        BufferedImage concatenatedImage = new BufferedImage(width, height, img1.getType());

        // Draw the first image onto the new image
        Graphics2D g2d = concatenatedImage.createGraphics();
        g2d.drawImage(img1, 0, 0, null);

        // Draw the second image shifted horizontally by the width of the first image
        g2d.drawImage(img2, img1.getWidth(), 0, null);
        g2d.dispose();

        return concatenatedImage;
    }

    public static BufferedImage concatenateImagesVertically(BufferedImage img1, BufferedImage img2) {
        // Get the width and height of the resulting image
        int width = Math.max(img1.getWidth(), img2.getWidth());
        int height = img1.getHeight() + img2.getHeight();

        // Create a new image with the desired dimensions
        BufferedImage concatenatedImage = new BufferedImage(width, height, img1.getType());

        // Draw the first image onto the new image
        Graphics2D g2d = concatenatedImage.createGraphics();
        g2d.drawImage(img1, 0, 0, null);

        // Draw the second image shifted vertically by the height of the first image
        g2d.drawImage(img2, 0, img1.getHeight(), null);
        g2d.dispose();

        return concatenatedImage;
    }

    public static BufferedImage compressImage(BufferedImage original, int factor) {
        int newWidth = original.getWidth() / factor;
        int newHeight = original.getHeight() / factor;
        BufferedImage compressedImage = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g2d = compressedImage.createGraphics();
        g2d.drawImage(original, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        return compressedImage;
    }

    public static BufferedImage getTransparentDummyImage(int width, int height, int type) {
        BufferedImage dummyImage = new BufferedImage(width, height, type);
        Graphics2D g2d = dummyImage.createGraphics();
        // Set the background to transparent
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, width, height);
        return dummyImage;
    }

}
