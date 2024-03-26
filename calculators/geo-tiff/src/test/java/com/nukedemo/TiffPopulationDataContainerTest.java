package com.nukedemo;

import org.junit.jupiter.api.Test;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.geometry.DirectPosition2D;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TiffPopulationDataContainerTest {

    String filepath = "./src/main/resources/GHS_POP_E2030_GLOBE_R2023A_4326_30ss_V1_0_R4_C20.tif";
    double lat = 12.546250343322754;
    double lon = 55.67041778564453;
    int x = 306, y = 411;

    @Test
    public void testTiffApp() throws Exception {
        TiffPopulationDataContainer tiffPopulationDataContainer = new TiffPopulationDataContainer(filepath);
        // convert lat/lon gps coordinates to tiff x/y coordinates
        System.out.println(tiffPopulationDataContainer.pixelDataFromCoord(lat, lon)[0]);
        System.out.println(tiffPopulationDataContainer.pixelDataFromXY(x, y)[0]);
        System.out.println(tiffPopulationDataContainer.coordFromXY(lat, lon));
        System.out.println(tiffPopulationDataContainer.xyFromCoord(x, y));
        System.out.println(tiffPopulationDataContainer.getMetaData());
        System.out.println(tiffPopulationDataContainer.toStringIntArrayPretty());
    }


    @Test
    public void testTiffPopulationDataContainerString() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        assertNotNull(container);
    }


    @Test
    public void testPngRendering() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        File outputFile = new File("test_res.png");
        byte[] res = container.writeToPngByteArray();
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(res);
        }
        assertTrue(outputFile.getTotalSpace() > 1000); //smthng hass been written.
        outputFile.delete();
    }

    @Test
    public void testTiffPopulationDataContainerFile() throws Exception {
        File file = new File(filepath);
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(file);
        assertNotNull(container);
    }

    @Test
    public void testPixelDataFromCoord() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        double[] pixelData = container.pixelDataFromCoord(lat, lon);

        assertNotNull(pixelData);
        assertTrue(pixelData.length > 0);
    }

    @Test
    public void testPixelDataFromXY() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        double[] pixelData = container.pixelDataFromXY(x, y);

        assertNotNull(pixelData);
        assertTrue(pixelData.length > 0);
    }

    @Test
    public void testCoordFromXY() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        GridCoordinates2D coord = container.coordFromXY(lat, lon);

        assertNotNull(coord);
    }

    @Test
    public void testXyFromCoord() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        DirectPosition2D pos = (DirectPosition2D) container.xyFromCoord(x, y);
        assertNotNull(pos);
    }

    @Test
    public void testCompression() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        int[] compressed = container.compressIntArray(10);
        assertNotNull(compressed);
    }

    @Test
    public void testCompressionCompatibility() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        int[] array = container.toIntArray();
        int[] compressedArray = container.compressIntArray(10);
        long originalTotal = 0l;
        long compressedTotal = 0l;
        for (int i = 0; i < array.length; i++) {
            originalTotal = originalTotal + array[i];
        }
        for (int i = 0; i < compressedArray.length; i++) {
            compressedTotal = compressedTotal + compressedArray[i];
        }
        assertEquals(originalTotal, compressedTotal);
        assertEquals(23554, compressedArray[0]); //23554 manually calculated.
    }


    @Test
    public void testCompatibility() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        String pretty = container.toStringIntArrayPretty().replace(System.lineSeparator(), "");
        int[] array = container.toIntArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);

            // Append comma if it's not the last element
            if (i < array.length - 1) {
                sb.append(",");
            }
        }
        String fromArr = sb.toString().replace(System.lineSeparator(), "");
        pretty = pretty.substring(0, pretty.length() - 1);
        assertEquals(pretty.length(), fromArr.length());
        assertEquals(pretty, fromArr);
    }

}