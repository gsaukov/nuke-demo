package com.nukedemo;


import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.awt.image.Raster;
import java.io.File;

public class TiffPopulationMollweideDataContainer {
    public static CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;

    private final GeoTiffReader reader;
    private final GridCoverage2D cov;
    private final Raster tiffRaster;
    private final int  rasterWidth;
    private final int  rasterHeight;

    public TiffPopulationMollweideDataContainer(String fileToProcess) throws Exception {
        this.reader = new GeoTiffReader(new File(fileToProcess));
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

}
