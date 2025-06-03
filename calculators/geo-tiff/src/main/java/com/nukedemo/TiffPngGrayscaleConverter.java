package com.nukedemo;

import org.geotools.coverage.grid.GridCoverage2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TiffPngGrayscaleConverter {

    public static byte[] convert(GridCoverage2D cov) throws IllegalArgumentException, IOException {
        int imageHeight = cov.getGridGeometry().getGridRange2D().height;
        int imageWidth = cov.getGridGeometry().getGridRange2D().width;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        int[] data = cov.getRenderedImage().getData().getPixels(0, 0, imageWidth, imageHeight, new int[imageHeight * imageWidth]);

        int idx = 0;
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int value = data[idx++];
                image.setRGB(x, y, value);
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);

        return out.toByteArray();
    }
}
