package com.nukedemo;

import com.nukedemo.shared.utils.NdJsonUtils;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void testMunichExtract() throws Exception {
        //lon, lat
        //48.295914, 11.333398
        //47.841293, 11.788019
        double [] topLeftCoord =  {11.333398, 48.295914};
        double [] bottomRightCoord =  {11.788019, 47.841293};
        TiffPopulationDataContainer container = new TiffPopulationDataContainer("./src/main/resources/GHS_POP_E2025_GLOBE_R2023A_4326_3ss_V1_0_R5_C20.tif");
        int[] arr = container.toIntArray();
        GridCoordinates2D topLeft = container.coordFromXY(topLeftCoord[0], topLeftCoord[1]);
        GridCoordinates2D bottomRight = container.coordFromXY(bottomRightCoord[0], bottomRightCoord[1]);
        int len = Math.max(Math.abs(bottomRight.x - topLeft.x), Math.abs(bottomRight.y - topLeft.y));
        //coordinates must be refined to the pixel positions
        double [] refinedTopLeftCoord = container.xyFromCoord(topLeft.x, topLeft.y).getCoordinate();
        double [] refinedBottomRightCoord = container.xyFromCoord(topLeft.x + len, topLeft.y + len).getCoordinate();
        int[] res = getBlock(arr, topLeft.y, topLeft.x, 12000, len);
        GhslMetaData baseMetaData = container.getMetaData();
        GhslMetaData metaData = GhslMetaData.builder()
                .withTopLeftCorner(refinedTopLeftCoord)
                .withBottomRightCorner(refinedBottomRightCoord)
                .withAreaWidth(len)
                .withAreaHeight(len)
                .withPixelHeightDegrees(baseMetaData.getPixelHeightDegrees())
                .withPixelWidthDegrees(baseMetaData.getPixelWidthDegrees())
                .build();
        toJson(metaData, res);
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

    @Test
    public void testImageSplitting() throws Exception {
        BufferedImage imageToSplit = ImageIO.read(new File("./src/main/resources/GHS_POP_E2025_GLOBE_R2023A_4326_3ss_V1_0_R4_C19.png"));
        List<List<BufferedImage>> splitImages = ImageTransformations.splitImage(imageToSplit, 3);
        List<File> tempFiles = new ArrayList<>();
        for(int i = 0; i<splitImages.size(); i++) {
            List<BufferedImage> row = splitImages.get(i);
            for(int j = 0; j<row.size(); j++) {
                BufferedImage image = row.get(j);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(image, "PNG", out);
                File outputFile = new File("test_res_"+j+"_"+i+".png");
                byte[] res = out.toByteArray();
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    fos.write(res);
                }
                tempFiles.add(outputFile);
            }
        }
        tempFiles.forEach(f->f.delete());
    }

    private int[] getBlock(int[] data, int rowStart, int columnStart, int cols, int length) {
        int[] block = new int[length*length];
        int pointer = 0;
        for(int i = rowStart; i < rowStart + length; i++) {
            for(int j = columnStart; j < columnStart + length; j++) {
                block[pointer] = data[(i*cols) + j];
                pointer++;
            }
        }
        return block;
    }

    private String toJson (GhslMetaData metaData, Object data) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("metaData",  metaData);
        objectMap.put("data",  data);
        return NdJsonUtils.toJson(objectMap);
    }

}