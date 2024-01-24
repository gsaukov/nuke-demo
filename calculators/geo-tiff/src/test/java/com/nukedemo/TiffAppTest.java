package com.nukedemo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TiffAppTest {



    @Test
    public void testTiffApp() throws Exception {
        String fileToProcess = "./src/main/resources/GHS_POP_E2030_GLOBE_R2023A_4326_30ss_V1_0_R4_C20.tif";
        TiffApp tiffApp = new TiffApp(fileToProcess);
        // convert lat/lon gps coordinates to tiff x/y coordinates
        double lat = 12.546250343322754;
        double lon = 55.67041778564453;
        System.out.println(tiffApp.pixelDataFromCoord(lat, lon)[0]);
        System.out.println(tiffApp.pixelDataFromXY(306, 411)[0]);
        System.out.println(tiffApp.coordFromXY(lat, lon));
        System.out.println(tiffApp.xyFromCoord(306, 411));
        System.out.println(tiffApp.toStringIntArray());
    }

}