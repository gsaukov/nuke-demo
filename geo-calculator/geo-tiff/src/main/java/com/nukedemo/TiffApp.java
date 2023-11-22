package com.nukedemo;


import java.awt.image.Raster;
import java.io.File;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

// Geotiff Mollweide and geotools 
// https://gis.stackexchange.com/questions/314421/geotiff-geotools-mollweide-customer-projection-not-recognized
// https://gis.stackexchange.com/questions/314282/geotiff-mollweide-and-geotools

public class TiffApp {

    public static void main( String[] args ) throws Exception {

        // based on: http://www.smartjava.org/content/access-information-geotiff-using-java/

        // load tiff file to memory
        File tiffFile = new File("./geo-calculator/geo-tiff/src/main/resources/GHS_POP_E2030_GLOBE_R2023A_4326_3ss_V1_0_R12_C8.tif");
        GeoTiffReader reader = new GeoTiffReader(tiffFile);
        GridCoverage2D cov = reader.read(null);
        Raster tiffRaster = cov.getRenderedImage().getData();

        // convert lat/lon gps coordinates to tiff x/y coordinates
        double lat = -25;
        double lon = -105;
        CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;
        GridGeometry2D gg = cov.getGridGeometry();
        DirectPosition2D posWorld = new DirectPosition2D(wgs84, lon, lat); // longitude supplied first
        GridCoordinates2D posGrid = gg.worldToGrid(posWorld);

        // sample tiff data with at pixel coordinate
        double[] rasterData = new double[1];
        tiffRaster.getPixel(posGrid.x, posGrid.y, rasterData);

        System.out.println(String.format("GeoTIFF data at %s, %s: %s", lat, lon, rasterData[0]));

    }


}
