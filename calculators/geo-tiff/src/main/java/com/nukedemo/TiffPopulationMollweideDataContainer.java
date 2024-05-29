package com.nukedemo;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.PrjFileReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.DirectPosition2D;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;


/*
* This class is still in development...
* //try mollwide conversion: https://gis.stackexchange.com/questions/221552/proj4j-not-precise-for-epsg3857-transformations
* */
public class TiffPopulationMollweideDataContainer {
    public CoordinateReferenceSystem mollweide;
    private final GeoTiffReader reader;
    private final GridCoverage2D cov;
    private final Raster tiffRaster;
    private final int  rasterWidth;
    private final int  rasterHeight;

    public TiffPopulationMollweideDataContainer(String fileToProcess) throws Exception {
        this.mollweide = getCrs();
        this.reader = new GeoTiffReader(new File(fileToProcess));
        this.cov = reader.read(null);
        //use tif raster minx/miny/ to find
        this.tiffRaster = cov.getRenderedImage().getTile(0, 0);
        this.rasterWidth = tiffRaster.getWidth();
        this.rasterHeight = tiffRaster.getHeight();
    }

    public double[] pixelDataFromCoord(double lat, double lon) throws Exception {
        GridGeometry2D gg = cov.getGridGeometry();
        DirectPosition2D posWorld = new DirectPosition2D(mollweide, lat, lon);
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
        DirectPosition2D posWorld = new DirectPosition2D(mollweide, lat, lon); // longitude supplied first
        return gg.worldToGrid(posWorld);
    }

    public DirectPosition xyFromCoord(int x, int y) throws Exception {
        GridCoordinates2D coord = new GridCoordinates2D(x, y);
        return cov.getGridGeometry().gridToWorld(coord);
    }

    static CoordinateReferenceSystem getCrs() throws Exception {
        FileInputStream instream = null;
        instream = new FileInputStream(new File("./src/main/resources/GHS_POP_E2030_GLOBE_R2023A_54009_100_V1_0_R7_C18.prj"));
        final FileChannel channel = instream.getChannel();
        PrjFileReader projReader  = new PrjFileReader(channel);
        CoordinateReferenceSystem crs = projReader.getCoordinateReferenceSystem();
        return crs;
    }

}
