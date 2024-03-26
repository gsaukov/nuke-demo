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
import java.io.File;
import java.io.IOException;

public class TiffPngConverter {

    public static void convert(GridCoverage2D cov) throws IllegalArgumentException, IOException {
        BufferedImage image = new BufferedImage(cov.getGridGeometry().getGridRange2D().width,
                cov.getGridGeometry().getGridRange2D().height, BufferedImage.TYPE_4BYTE_ABGR);
        MapContent mapContent = new MapContent();
        mapContent.getViewport().setCoordinateReferenceSystem(cov.getCoordinateReferenceSystem());
        Layer rasterLayer = new GridCoverageLayer(cov, createStyle());
        mapContent.addLayer(rasterLayer);
        GTRenderer draw = new StreamingRenderer();
        draw.setMapContent(mapContent);
        Graphics2D graphics = image.createGraphics();
        draw.paint(graphics, cov.getGridGeometry().getGridRange2D(), mapContent.getMaxBounds());
        File out = new File("test.png");
        ImageIO.write(image, "PNG", out);
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
