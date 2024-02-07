package com.nukedemo;

import org.junit.jupiter.api.Test;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.geometry.DirectPosition2D;
import java.io.File;

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
        System.out.println(tiffPopulationDataContainer.toStringIntArrayPretty());
    }


    @Test
    public void testTiffPopulationDataContainerString() throws Exception {
        TiffPopulationDataContainer container = new TiffPopulationDataContainer(filepath);
        assertNotNull(container);
    }

    @Test
    public void testTiffPopulationDataContainerFile() throws Exception  {
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

}