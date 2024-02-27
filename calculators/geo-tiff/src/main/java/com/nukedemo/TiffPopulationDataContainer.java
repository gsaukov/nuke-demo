package com.nukedemo;


import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

// Geotiff Mollweide and geotools 
// https://gis.stackexchange.com/questions/314421/geotiff-geotools-mollweide-customer-projection-not-recognized
// https://gis.stackexchange.com/questions/314282/geotiff-mollweide-and-geotools
// based on: http://www.smartjava.org/content/access-information-geotiff-using-java/

public class TiffPopulationDataContainer {

    public static CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;
    public static String LICENSE = "(CC BY 4.0) Data Source: EC-GHSL https://ghsl.jrc.ec.europa.eu/";
    private final byte[] source;
    private final GeoTiffReader reader;
    private final GridCoverage2D cov;
    private final Raster tiffRaster;
    private final int rasterWidth;
    private final int rasterHeight;

    public TiffPopulationDataContainer(String fileToProcess) throws Exception {
        this(new File(fileToProcess));
    }

    public TiffPopulationDataContainer(File fileToProcess) throws Exception {
        this(FileUtils.readFileToByteArray(fileToProcess));
    }

    public TiffPopulationDataContainer(byte[] bytes) throws Exception {
        this.source = bytes;
        this.reader = new GeoTiffReader(new ByteArrayInputStream(this.source));
        this.cov = reader.read(null);
        this.tiffRaster = cov.getRenderedImage().getData();
        this.rasterWidth = tiffRaster.getWidth();
        this.rasterHeight = tiffRaster.getHeight();
    }

    public double[] pixelDataFromCoord(double lat, double lon) throws Exception {
        GridGeometry2D gg = cov.getGridGeometry();
        DirectPosition2D posWorld = new DirectPosition2D(wgs84, lat, lon);
        GridCoordinates2D posGrid = gg.worldToGrid(posWorld);
        double[] rasterData = new double[1];
        tiffRaster.getPixel(posGrid.x, posGrid.y, rasterData);
        return rasterData;
    }

    public double[] pixelDataFromXY(int x, int y) throws Exception {
        double[] rasterData = new double[1];
        tiffRaster.getPixel(x, y, rasterData);
        return rasterData;
    }

    public GridCoordinates2D coordFromXY(double lat, double lon) throws Exception {
        GridGeometry2D gg = cov.getGridGeometry();
        DirectPosition2D posWorld = new DirectPosition2D(wgs84, lat, lon); // longitude supplied first
        return gg.worldToGrid(posWorld);
    }

    public DirectPosition xyFromCoord(int x, int y) throws Exception {
        GridCoordinates2D coord = new GridCoordinates2D(x, y);
        return cov.getGridGeometry().gridToWorld(coord);
    }

    public int[] toIntArray() {
        return tiffRaster.getPixels(0, 0, rasterWidth, rasterHeight, new int[rasterHeight * rasterWidth]);
    }

    public double[] toDoubleArray() {
        return tiffRaster.getPixels(0, 0, rasterWidth, rasterHeight, new double[rasterHeight * rasterWidth]);
    }

    public GhslMetaData getMetaData() {
        //GHS_POP_E2030_GLOBE_R2023A_4326_30ss_V1_0_R4_C20
        //[lon,lat]
        //top left 10.0348, 59.0495
        //bottom left 10.0123, 49.0981
        //top right 19.921, 58.998
        //bottom right 19.921, 49.215
        double[] topRightCorner = cov.getGridGeometry().getEnvelope().getUpperCorner().getCoordinate();
        double[] bottomLeftCorner = cov.getGridGeometry().getEnvelope().getLowerCorner().getCoordinate();
        double[] topLeftCorner = new double[]{bottomLeftCorner[0], topRightCorner[1]};
        double[] bottomRightCorner = new double[]{topRightCorner[0], bottomLeftCorner[1]};
        //Measurement units degrees
        double pixelHeight = Math.abs(topRightCorner[1] - bottomRightCorner[1]) / rasterHeight;
        double pixelWidth = Math.abs(bottomLeftCorner[0] - bottomRightCorner[0]) / rasterWidth;
        int[] totals = getPixelTotals();
        return GhslMetaData.builder()
                .withAreaWidth(rasterWidth)
                .withAreaHeight(rasterHeight)
                .withTopRightCorner(topRightCorner)
                .withBottomLeftCorner(bottomLeftCorner)
                .withTopLeftCorner(topLeftCorner)
                .withBottomRightCorner(bottomRightCorner)
                .withPixelHeightDegrees(pixelHeight)
                .withPixelWidthDegrees(pixelWidth)
                .withTotalPixelCount(totals[0])
                .withTotalPixelValue(totals[1])
                .withLicense(LICENSE)
                .build();
    }

    public int[] getPixelTotals() {
        int totalCount = 0;
        double totalValue = 0;
        double[] arr = toDoubleArray();
        for (int i = 0; i < arr.length; i++) {
            double pixel = arr[i];
            if (pixel > 0) {
                totalCount++;
                totalValue = totalValue + pixel;
            }
        }
        return new int[]{totalCount, (int) Math.round(totalValue)};
    }

    public int[] compressIntArray(int factor) {
        if (rasterHeight % factor != 0 || rasterWidth % factor != 0)
            throw new IllegalArgumentException("invalid factor " + factor + " must be round to round to width:" + rasterWidth + " and height: " + rasterHeight);
        int[] original = toIntArray();
        int finalSize = (rasterHeight * rasterWidth) / factor;
        int twoD[][] = toTwoDInt();

        int[][] compressedArray = new int[rasterHeight/factor][rasterWidth/factor]; // New dimensions: 120x120
        for (int i = 0; i < 1200; i += factor) {
            for (int j = 0; j < 1200; j += factor) {
                int sum = 0;
                for (int x = i; x < i + factor; x++) {
                    for (int y = j; y < j + factor; y++) {
                        sum += twoD[x][y];
                    }
                }
                compressedArray[i / factor][j / factor] = sum;
            }
        }

        return toOneDInt(compressedArray, (rasterHeight*rasterHeight)/factor);
    }

    public int[][] toTwoDInt() {
        int arrayIndex = 0;
        int[] original = toIntArray();
        int twoD[][] = new int[rasterHeight][rasterWidth];
        for (int i = 0; i < rasterHeight; i++) {
            for (int j = 0; j < rasterWidth; j++){
                twoD[i][j] = original[arrayIndex];
                arrayIndex++;
            }
        }
        return twoD;
    }

    public int[] toOneDInt(int[][] data, int size) {
        int arrayIndex = 0;
        int [] oneD = new int [size];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[i].length; j++){
                oneD[arrayIndex] = data[i][j];
                arrayIndex++;
            }
        }

        return oneD;
    }

    public String toStringIntArrayPretty() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < rasterWidth * rasterHeight; i++) {
            s.append(tiffRaster.getDataBuffer().getElem(i) + ",");
            if (i > 0 && i % rasterWidth == 0) {
                s.append(System.lineSeparator());
            }
        }
        return s.toString();
    }

    public String toStringDoubleArrayPretty() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < rasterWidth * rasterHeight; i++) {
            s.append(tiffRaster.getDataBuffer().getElemDouble(i) + ",");
            if (i > 0 && i % rasterWidth == 0) {
                s.append(System.lineSeparator());
            }
        }
        return s.toString();
    }

    public void printMaxes() {
        StringBuffer s = new StringBuffer();
        int max = 0;
        for (int i = 0; i < rasterWidth * rasterHeight; i++) {
            int el = tiffRaster.getDataBuffer().getElem(i);
            if (el > 1000) {
                System.out.println(i + " " + i / rasterWidth + " " + i % rasterWidth + " " + el);
                max = Math.max(max, el);
            }
        }
        System.out.println(max);
    }

}
