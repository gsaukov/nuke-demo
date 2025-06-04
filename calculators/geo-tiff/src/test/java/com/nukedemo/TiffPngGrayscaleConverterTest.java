package com.nukedemo;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class TiffPngGrayscaleConverterTest {

    String filepath = "./src/main/resources/GHS_POP_E2030_GLOBE_R2023A_4326_30ss_V1_0_R4_C20.tif";
    @Test
    public void testGrayscalePngRendering() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        container.writeToPngGrayscaleByteArray();
        File outputFile = new File("test_res.png");
        byte[] res = container.writeToPngGrayscaleByteArray();
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(res);
        }
        assertTrue(outputFile.getTotalSpace() > 1000); //smthng hass been written.
        outputFile.delete();
    }

}
