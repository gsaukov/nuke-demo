package com.nukedemo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TiffPopulationDataContainerTest {



    @Test
    public void testTiffApp() throws Exception {
        String fileToProcess = "./src/main/resources/GHS_POP_E2030_GLOBE_R2023A_4326_30ss_V1_0_R4_C20.tif";
        TiffPopulationDataContainer tiffPopulationDataContainer = new TiffPopulationDataContainer(fileToProcess);
        // convert lat/lon gps coordinates to tiff x/y coordinates
        double lat = 12.546250343322754;
        double lon = 55.67041778564453;
        System.out.println(tiffPopulationDataContainer.pixelDataFromCoord(lat, lon)[0]);
        System.out.println(tiffPopulationDataContainer.pixelDataFromXY(306, 411)[0]);
        System.out.println(tiffPopulationDataContainer.coordFromXY(lat, lon));
        System.out.println(tiffPopulationDataContainer.xyFromCoord(306, 411));
        System.out.println(tiffPopulationDataContainer.toStringIntArray());
    }

}