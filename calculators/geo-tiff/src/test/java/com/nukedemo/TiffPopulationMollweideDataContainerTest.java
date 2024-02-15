package com.nukedemo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TiffPopulationMollweideDataContainerTest {

    String filepath = "./src/main/resources/GHS_POP_E2030_GLOBE_R2023A_54009_100_V1_0_R7_C18.tif";
    double lat = 12.546250343322754;
    double lon = 55.67041778564453;
    int x = 150, y = 100;

    @Test
    public void testTiffApp() throws Exception {
        TiffPopulationMollweideDataContainer tiffPopulationDataContainer = new TiffPopulationMollweideDataContainer(filepath);
        System.out.println(tiffPopulationDataContainer.pixelDataFromXY(x, y)[0]);
        System.out.println(tiffPopulationDataContainer.xyFromCoord(x, y));
        System.out.println(tiffPopulationDataContainer.coordFromXY(lat, lon));
        System.out.println(tiffPopulationDataContainer.pixelDataFromCoord(lat, lon)[0]);
    }

}