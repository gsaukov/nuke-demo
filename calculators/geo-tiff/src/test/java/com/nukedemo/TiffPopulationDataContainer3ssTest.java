package com.nukedemo;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static com.nukedemo.TiffPngConverter.DENSITY_3SS;
import static org.junit.jupiter.api.Assertions.*;

class TiffPopulationDataContainer3ssTest {

    String filepath = "./src/main/resources/GHS_POP_E2030_GLOBE_R2023A_4326_3ss_V1_0_R12_C8.tif";
    @Test
    public void testPngRendering() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        File outputFile = new File("test_res.png");
        byte[] res = container.writeToPngByteArray(DENSITY_3SS);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(res);
        }
        assertTrue(outputFile.getTotalSpace() > 1000); //smthng hass been written.
        outputFile.delete();
    }

    @Test
    public void testImageConcatenation() throws Exception {
        BufferedImage a = ImageIO.read(new File("./src/main/resources/GHS_POP_E2025_GLOBE_R2023A_4326_30ss_V1_0_R4_C20.png"));
        BufferedImage b = ImageIO.read(new File("./src/main/resources/GHS_POP_E2025_GLOBE_R2023A_4326_30ss_V1_0_R5_C20.png"));
        BufferedImage c = ImageIO.read(new File("./src/main/resources/GHS_POP_E2025_GLOBE_R2023A_4326_30ss_V1_0_R6_C20.png"));
        BufferedImage d = ImageTransformations.concatenateImagesVertically(a, b);
        BufferedImage e = ImageTransformations.concatenateImagesVertically(d, c);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(e, "PNG", out);
        File outputFile = new File("test_res.png");
        byte[] res = out.toByteArray();
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(res);
        }
        outputFile.delete();
    }

}