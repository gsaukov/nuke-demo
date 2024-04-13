package com.nukedemo;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.*;
import org.opengis.filter.FilterFactory2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
 *  Bleak Color Map:
 * 	color: #00FF00 value: 100
 * 	color: #00FF00 value: 1000
 *  color: #00FFFF value: 5000
 *  color: #0000FF value: 10000
 *
 *  Intense Color Map:
 * 	color: #f4fbf2 value: 20-99
 * 	color: #d9f2e5 value: 100-399
 *  color: #a8e3e5 value: 400-1k
 *  color: #71c7d7 value: 1k-2k
 * 	color: #428acb value: 2k-3.5k
 *  color: #2d6bb3 value: 3.5k-5.5k
 * 	color: #0c4c9f value: 5.5k-7.5k
 *  color: #00309f value: 7.5k-10k
 * 	color: #521f8b value: 10k-12k
 *  color: #700080 value: 12k-16k
 * 	color: #990049 value: 16k-22k
 *  color: #cc003d value: 22k-30k
 * 	color: #ff0000 value: 30k-50k
 *  color: #ff6200 value: 50k-100k
 * 	color: #ff9e00 value: 100k-200k
 *  color: #ffc300 value: 200k+
 * */


public class TiffPngConverter {

    public static final int DENSITY_3SS = 1;
    public static int DENSITY_30SS = 100;

    public static byte[] convert(GridCoverage2D cov, int density) throws IllegalArgumentException, IOException {
        MapContent mapContent = new MapContent();
        try {
            BufferedImage image = new BufferedImage(cov.getGridGeometry().getGridRange2D().width,
                    cov.getGridGeometry().getGridRange2D().height, BufferedImage.TYPE_4BYTE_ABGR);
            mapContent.getViewport().setCoordinateReferenceSystem(cov.getCoordinateReferenceSystem());
            Layer rasterLayer = new GridCoverageLayer(cov, createStyle(density));
            mapContent.addLayer(rasterLayer);
            GTRenderer draw = new StreamingRenderer();
            draw.setMapContent(mapContent);
            Graphics2D graphics = image.createGraphics();
            draw.paint(graphics, cov.getGridGeometry().getGridRange2D(), mapContent.getMaxBounds());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", out);
            return out.toByteArray();
        } finally {
            mapContent.dispose();
        }
    }

    private static Style createStyle(int density) {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        StyleFactory sf = CommonFactoryFinder.getStyleFactory();
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        sym.setColorMap(intense(ff, sf, 1, density));
        Style style = SLD.wrapSymbolizers(sym);
        return style;
    }

    private static ColorMap bleak(FilterFactory2 ff, StyleFactory sf, double opacity, int density) {
        ColorMap cMap = sf.createColorMap();
        fillColorMap(ff, sf, cMap, "#00FF00", getOpacity(0.0, opacity), 0.0 * density);
        fillColorMap(ff, sf, cMap, "#00FF00", getOpacity(0.2, opacity), 0.01 * density);
        fillColorMap(ff, sf, cMap, "#00FF00", getOpacity(0.3, opacity), 1.0 * density);
        fillColorMap(ff, sf, cMap, "#00FFFF", getOpacity(0.4, opacity), 10.0 * density);
        fillColorMap(ff, sf, cMap, "#0000FF", getOpacity(0.4, opacity), 50.0 * density);
        fillColorMap(ff, sf, cMap, "#0000FF", getOpacity(0.5, opacity), 100.0 * density);
        return cMap;
    }


    private static ColorMap intense(FilterFactory2 ff, StyleFactory sf, double opacity, int density) {
        ColorMap cMap = sf.createColorMap();
        fillColorMap(ff, sf, cMap, "#00FF00", getOpacity(0.0, opacity), 0.0 * density);  //color: transparent value: 0-10
        fillColorMap(ff, sf, cMap, "#f4fbf2", getOpacity(0.1, opacity), 0.01 * density);  //color: #f4fbf2 value: 10-49
        fillColorMap(ff, sf, cMap, "#f4fbf2", getOpacity(0.2, opacity), 1.0 * density);  //color: #f4fbf2 value: 50-129
        fillColorMap(ff, sf, cMap, "#d9f2e5", getOpacity(0.2, opacity), 2.0 * density);  //color: #d9f2e5 value: 130-399
        fillColorMap(ff, sf, cMap, "#a8e3e5", getOpacity(0.2, opacity), 6.0 * density);  //color: #a8e3e5 value: 400-1k
        fillColorMap(ff, sf, cMap, "#71c7d7", getOpacity(0.3, opacity), 15.0 * density);  //color: #71c7d7 value: 1k-2k
        fillColorMap(ff, sf, cMap, "#428acb", getOpacity(0.3, opacity), 27.0 * density);  //color: #428acb value: 2k-3.5k
        fillColorMap(ff, sf, cMap, "#2d6bb3", getOpacity(0.3, opacity), 42.0 * density);  //color: #2d6bb3 value: 3.5k-5.5k
        fillColorMap(ff, sf, cMap, "#0c4c9f", getOpacity(0.3, opacity), 62.0 * density);  //color: #0c4c9f value: 5.5k-7.5k
        fillColorMap(ff, sf, cMap, "#00309f", getOpacity(0.3, opacity), 82.0 * density);  //color: #00309f value: 7.5k-10k
        fillColorMap(ff, sf, cMap, "#521f8b", getOpacity(0.3, opacity), 110.0 * density);  //color: #521f8b value: 10k-12k
        fillColorMap(ff, sf, cMap, "#700080", getOpacity(0.3, opacity), 140.0 * density);  //color: #700080 value: 12k-16k
        fillColorMap(ff, sf, cMap, "#990049", getOpacity(0.4, opacity), 190.0 * density);  //color: #990049 value: 16k-22k
        fillColorMap(ff, sf, cMap, "#cc003d", getOpacity(0.4, opacity), 260.0 * density);  //color: #cc003d value: 22k-30k
        fillColorMap(ff, sf, cMap, "#ff0000", getOpacity(0.4, opacity), 400.0 * density);  //color: #ff0000 value: 30k-50k
        fillColorMap(ff, sf, cMap, "#ff6200", getOpacity(0.5, opacity), 750.0 * density);  //color: #ff6200 value: 50k-100k
        fillColorMap(ff, sf, cMap, "#ff9e00", getOpacity(0.5, opacity), 1500.0 * density);  //color: #ff9e00 value: 100k-200k
        fillColorMap(ff, sf, cMap, "#ffc300", getOpacity(0.5, opacity), 3000.0 * density);  //color: #ffc300 value: 200k+
        return cMap;
    }

    private static double getOpacity(double value, double coefficient) {
        return Math.min(value * coefficient, 1.0);
    }

    private static void fillColorMap(FilterFactory2 ff, StyleFactory sf, ColorMap cMap, String color, double opacity, double value) {
        ColorMapEntry entry = sf.createColorMapEntry();
        entry.setColor(ff.literal(color));
        entry.setOpacity(ff.literal(opacity));
        entry.setQuantity(ff.literal(value));
        cMap.addColorMapEntry(entry);
    }

}
