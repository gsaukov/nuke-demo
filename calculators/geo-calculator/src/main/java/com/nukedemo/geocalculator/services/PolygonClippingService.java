package com.nukedemo.geocalculator.services;

import com.mapbox.geojson.Feature;
import com.menecats.polybool.Epsilon;
import com.menecats.polybool.PolyBool;
import com.menecats.polybool.models.Polygon;
import com.menecats.polybool.models.geojson.Geometry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.menecats.polybool.helpers.PolyBoolHelper.*;
import static com.menecats.polybool.helpers.PolyBoolHelper.point;

@Slf4j
@Service
public class PolygonClippingService {

    private static final Epsilon EPSILON = epsilon();

    public com.mapbox.geojson.Geometry union(List<Feature> features) throws Exception {
        List<Geometry.MultiPolygonGeometry> polygons = getPolygons(features);
        if (polygons.isEmpty()) {
            return null;
        }
        Geometry.MultiPolygonGeometry cumulative = polygons.get(0);
        if (polygons.size() < 2) {
            return toTurfGeometry(cumulative);
        }
        for (int i = 1; i < polygons.size(); i++) {//skip first
//            cumulative = PolyBool.union(EPSILON, cumulative, polygons.get(i));;
        }
        return toTurfGeometry(cumulative);
    }

    private com.mapbox.geojson.Geometry toTurfGeometry(Geometry.MultiPolygonGeometry cumulative) {
        return com.mapbox.geojson.MultiPolygon.fromPolygons(new ArrayList<>());
    }

    public List<Geometry.MultiPolygonGeometry> getPolygons(List<com.mapbox.geojson.Feature> features) {
        List<Geometry.MultiPolygonGeometry> polygons = features.stream()
                .filter(f -> (f.geometry() instanceof com.mapbox.geojson.Polygon))
                .map(f -> toPolygon((com.mapbox.geojson.Polygon)f.geometry())).collect(Collectors.toList());

        List<Geometry.MultiPolygonGeometry> multiPolygons = features.stream()
                .filter(f -> (f.geometry() instanceof com.mapbox.geojson.MultiPolygon))
                .map(f -> toMultiPolygon((com.mapbox.geojson.MultiPolygon)f.geometry()))
                .collect(Collectors.toList());
        polygons.addAll(multiPolygons);
        return polygons;
    }

    private Geometry.MultiPolygonGeometry toMultiPolygon(com.mapbox.geojson.MultiPolygon multiPolygon) {
        List<com.mapbox.geojson.Polygon> turfMultiPolygon = multiPolygon.polygons();
        for (com.mapbox.geojson.Polygon polygon : turfMultiPolygon) {

        }
        return new Geometry.MultiPolygonGeometry();
    }


    public Geometry.MultiPolygonGeometry toPolygon(com.mapbox.geojson.Polygon turfPolygon) {
        return new Geometry.MultiPolygonGeometry();
    }

    private void sample( ) {
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
