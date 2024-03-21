package com.nukedemo.geocalculator.services;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.menecats.polybool.PolyBool;
import com.menecats.polybool.models.Polygon;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static com.menecats.polybool.helpers.PolyBoolHelper.*;
import static com.menecats.polybool.helpers.PolyBoolHelper.point;
import static com.nukedemo.geocalculator.services.PolygonMerginRecursiveTask.EPSILON;

@Slf4j
class PolygonClippingServiceTest {

    PolygonClippingService service;

    @BeforeEach
    void setUp() {
        service = new PolygonClippingService();
    }

    @Test
    void unionMergeSimple() throws Exception {
        FeatureCollection featureCollection = getFeatureCollection("union_hole_simple.json");
        Geometry geometry = service.union(featureCollection.features());
        log.info("union merge simple: " + geometry.toJson());
    }

    @Test
    void unionMergeAdvanced() throws Exception {
        FeatureCollection featureCollection = getFeatureCollection("union_hole_double.json");
        Geometry geometry = service.union(featureCollection.features());
        log.info("union merge advance: " + geometry.toJson());
    }

    FeatureCollection getFeatureCollection(String fileName) throws IOException {
        Path path = new PathMatchingResourcePatternResolver().getResource(fileName).getFile().toPath();
        String geoJson = Files.readString(path, StandardCharsets.UTF_8);
        return FeatureCollection.fromJson(geoJson);
    }

    private void sample() {
        List<Polygon> pols = Arrays.asList(
                polygon(
                        region(
                                point(9.992083316153526, 49.09958333862941),
                                point(9.992083316153526, 49.09124917199628),
                                point(10.000417482787183, 49.09124917199628),
                                point(10.000417482787183, 49.09958333862941),
                                point(9.992083316153526, 49.09958333862941)
                        )),
                polygon(
                        region(
                                point(10.00041664945332, 49.09958333862941),
                                point(10.00041664945332, 49.09124917199628),
                                point(10.008750816086978, 49.09124917199628),
                                point(10.008750816086978, 49.09958333862941),
                                point(10.00041664945332, 49.09958333862941)
                        )),
                polygon(
                        region(
                                point(10.008749982753116, 49.09958333862941),
                                point(10.008749982753116, 49.09124917199628),
                                point(10.017084149386774, 49.09124917199628),
                                point(10.017084149386774, 49.09958333862941),
                                point(10.008749982753116, 49.09958333862941)
                        )),
                polygon(
                        region(
                                point(9.992083316153526, 49.09125000532908),
                                point(9.992083316153526, 49.08291583869595),
                                point(10.000417482787183, 49.08291583869595),
                                point(10.000417482787183, 49.09125000532908),
                                point(9.992083316153526, 49.09125000532908)
                        )),
                polygon(
                        region(
                                point(10.008749982753116, 49.09125000532908),
                                point(10.008749982753116, 49.08291583869595),
                                point(10.017084149386774, 49.08291583869595),
                                point(10.017084149386774, 49.09125000532908),
                                point(10.008749982753116, 49.09125000532908)
                        )),
                polygon(
                        region(
                                point(9.992083316153526, 49.08291667202875),
                                point(9.992083316153526, 49.074582505395625),
                                point(10.000417482787183, 49.074582505395625),
                                point(10.000417482787183, 49.08291667202875),
                                point(9.992083316153526, 49.08291667202875)
                        )),
                polygon(
                        region(
                                point(10.00041664945332, 49.08291667202875),
                                point(10.00041664945332, 49.074582505395625),
                                point(10.008750816086978, 49.074582505395625),
                                point(10.008750816086978, 49.08291667202875),
                                point(10.00041664945332, 49.08291667202875)
                        )),
                polygon(
                        region(
                                point(10.008749982753116, 49.08291667202875),
                                point(10.008749982753116, 49.074582505395625),
                                point(10.017084149386774, 49.074582505395625),
                                point(10.017084149386774, 49.08291667202875),
                                point(10.008749982753116, 49.08291667202875)
                        ))

        );

        Polygon cum = pols.get(0);
        for (int i = 1; i < pols.size(); i++) {
            cum = PolyBool.union(EPSILON, cum, pols.get(i));
        }
        log.info(cum.toString());
    }

}