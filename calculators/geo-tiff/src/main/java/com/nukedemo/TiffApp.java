package com.nukedemo;


import java.awt.image.Raster;
import java.io.File;

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

public class TiffApp {

    public static void main( String[] args ) throws Exception {
        String fileToProcess = "./geo-calculator/geo-tiff/src/main/resources/GHS_POP_E2030_GLOBE_R2023A_4326_30ss_V1_0_R4_C20.tif";
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

    public static CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;

    private final File tiffFile;
    private final GeoTiffReader reader;
    private final GridCoverage2D cov;
    private final Raster tiffRaster;
    private final int  rasterWidth;
    private final int  rasterHeight;

    public TiffApp(String fileToProcess) throws Exception {
        this.tiffFile = new File(fileToProcess);
        this.reader = new GeoTiffReader(tiffFile);
        this.cov = reader.read(null);
        this.tiffRaster = cov.getRenderedImage().getData();
        this.rasterWidth = tiffRaster.getWidth();
        this.rasterHeight = tiffRaster.getHeight();
    }

    private double[] pixelDataFromCoord(double lat, double lon) throws Exception {
        GridGeometry2D gg = cov.getGridGeometry();
        DirectPosition2D posWorld = new DirectPosition2D(wgs84, lat, lon);
        GridCoordinates2D posGrid = gg.worldToGrid(posWorld);
        double[] rasterData = new double[1];
        tiffRaster.getPixel(posGrid.x, posGrid.y, rasterData);
        return rasterData;
    }

    private double[] pixelDataFromXY(int x, int y) throws Exception {
        double[] rasterData = new double[1];
        tiffRaster.getPixel(x, y, rasterData);
        return rasterData;
    }

    private GridCoordinates2D coordFromXY(double lat, double lon) throws Exception {
        GridGeometry2D gg = cov.getGridGeometry();
        DirectPosition2D posWorld = new DirectPosition2D(wgs84, lat, lon); // longitude supplied first
        return gg.worldToGrid(posWorld);
    }

    private DirectPosition xyFromCoord(int x, int y) throws Exception {
        GridCoordinates2D coord = new GridCoordinates2D(x, y);
        return cov.getGridGeometry().gridToWorld(coord);
    }

    private String toStringIntArray() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < rasterWidth * rasterHeight; i++) {
            s.append(tiffRaster.getDataBuffer().getElem(i) + ",");
            if (i > 0 && i % rasterWidth == 0) {
                s.append(System.lineSeparator());
            }
        }
        return s.toString();
    }

    private String toStringDoubleArray() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < rasterWidth * rasterHeight; i++) {
            s.append(tiffRaster.getDataBuffer().getElemDouble(i) + ",");
            if (i > 0 && i % rasterWidth == 0) {
                s.append(System.lineSeparator());
            }
        }
        return s.toString();
    }


    private void printMaxes() {
        StringBuffer s = new StringBuffer();
        int max = 0;
        for (int i = 0; i < rasterWidth * rasterHeight; i++) {
            int el = tiffRaster.getDataBuffer().getElem(i);
            if (el > 1000) {
                System.out.println(i + " " +i/rasterWidth + " " + i%rasterWidth + " " + el);
                max = Math.max(max, el);
            }
        }
        System.out.println(max);
    }

}
