package com.nukedemo;


import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.File;

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
    private final byte[] source;
    private final GeoTiffReader reader;
    private final GridCoverage2D cov;
    private final Raster tiffRaster;
    private final int  rasterWidth;
    private final int  rasterHeight;

    public TiffPopulationDataContainer(String fileToProcess) throws Exception {
        this(new File(fileToProcess));
    }

    public TiffPopulationDataContainer(File fileToProcess) throws Exception {
        this(FileUtils.readFileToByteArray(fileToProcess) );
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

    public String toStringIntArray() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < rasterWidth * rasterHeight; i++) {
            s.append(tiffRaster.getDataBuffer().getElem(i) + ",");
            if (i > 0 && i % rasterWidth == 0) {
                s.append(System.lineSeparator());
            }
        }
        return s.toString();
    }

    public String toStringDoubleArray() {
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
                System.out.println(i + " " +i/rasterWidth + " " + i%rasterWidth + " " + el);
                max = Math.max(max, el);
            }
        }
        System.out.println(max);
    }

}
