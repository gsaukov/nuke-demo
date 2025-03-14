package com.nukedemo.core;

import com.nukedemo.shared.utils.NdJsonUtils;
import com.nukedemo.geocalculator.services.TurfGeospatialService;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
import org.geojson.Polygon;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest(classes=CoreApplication.class)
@Slf4j
@SpringBatchTest
public class JsLibraryInvocationTest {

    @Autowired
    TurfGeospatialService turfGeospatialService;

    @Test
    public void testOverpassClient() throws Exception {
        FeatureCollection features = NdJsonUtils.fromJson(param, FeatureCollection.class);
        Polygon source = (Polygon)features.getFeatures().get(0).getGeometry();
        BigDecimal res = turfGeospatialService.calculateArea(NdJsonUtils.toJson(source.getCoordinates()));
        log.info(res.toString());
    }

    private String param = "{\"type\":\"FeatureCollection\",\"generator\":\"overpass-ide\",\"copyright\":\"Thedataincludedinthisdocumentisfromwww.openstreetmap.org.ThedataismadeavailableunderODbL.\",\"timestamp\":\"2023-02-24T17:46:38Z\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"@id\":\"way/612169247\",\"building\":\"yes\",\"military\":\"barracks\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-104.775668,38.7362686],[-104.7756934,38.7362871],[-104.7758194,38.7361817],[-104.7758,38.7361676],[-104.77588,38.7361007],[-104.775854,38.7360818],[-104.7758628,38.7360745],[-104.775834,38.7360536],[-104.7758534,38.7360374],[-104.7758141,38.7360088],[-104.7757905,38.7360286],[-104.7757603,38.7360066],[-104.7756793,38.7360745],[-104.7757058,38.7360938],[-104.7756996,38.736099],[-104.7757226,38.7361157],[-104.7756787,38.7361525],[-104.7756499,38.7361315],[-104.7755699,38.7361984],[-104.7755979,38.7362188],[-104.7755698,38.7362424],[-104.7756391,38.7362927],[-104.775668,38.7362686]]]},\"id\":\"way/612169247\"}]}";


}
