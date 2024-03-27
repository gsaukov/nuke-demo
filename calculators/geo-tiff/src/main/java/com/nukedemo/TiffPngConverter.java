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
 *  Possible Color Map:
 *
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

    public static byte[] convert(GridCoverage2D cov) throws IllegalArgumentException, IOException {
        MapContent mapContent = new MapContent();
        try {
            BufferedImage image = new BufferedImage(cov.getGridGeometry().getGridRange2D().width,
                    cov.getGridGeometry().getGridRange2D().height, BufferedImage.TYPE_4BYTE_ABGR);
            mapContent.getViewport().setCoordinateReferenceSystem(cov.getCoordinateReferenceSystem());
            Layer rasterLayer = new GridCoverageLayer(cov, createStyle());
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

    private static Style createStyle() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        StyleFactory sf = CommonFactoryFinder.getStyleFactory();

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ColorMap cMap = sf.createColorMap();
        ColorMapEntry start = sf.createColorMapEntry();
        start.setColor(ff.literal("#00FF00"));
        start.setOpacity(ff.literal(0.0));
        start.setQuantity(ff.literal(0.0));
        ColorMapEntry end = sf.createColorMapEntry();
        end.setColor(ff.literal("#00FF00"));
        end.setOpacity(ff.literal(0.3));
        end.setQuantity(ff.literal(100.0));
        ColorMapEntry ext = sf.createColorMapEntry();
        ext.setColor(ff.literal("#00FFFF"));
        ext.setOpacity(ff.literal(0.4));
        ext.setQuantity(ff.literal(1000.0));
        ColorMapEntry ext2 = sf.createColorMapEntry();
        ext2.setColor(ff.literal("#0000FF"));
        ext2.setOpacity(ff.literal(0.5));
        ext2.setQuantity(ff.literal(10000.0));

        cMap.addColorMapEntry(start);
        cMap.addColorMapEntry(end);
        cMap.addColorMapEntry(ext);
        cMap.addColorMapEntry(ext2);

        sym.setColorMap(cMap);

        Style style = SLD.wrapSymbolizers(sym);

        return style;
    }


}
