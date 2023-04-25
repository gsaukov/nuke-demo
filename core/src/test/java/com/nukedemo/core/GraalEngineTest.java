package com.nukedemo.core;

import com.nukedemo.core.services.exceptions.NdException;
import com.nukedemo.core.services.utils.NdJsonUtils;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.extern.slf4j.Slf4j;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.Polygon;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.script.ScriptEngine;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

@Slf4j
public class GraalEngineTest {

    {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
    }

    private static final String TURF_LIBRARY = "classpath:scripts/turf.js";

    @Test
    public void testTurfOnRandomCoordinatesReuseEngine() throws Exception {
        Random r = new Random();
        Polygon source = (Polygon) getFeature().getGeometry();
        ScriptEngine engine = graalJSScriptEngine();
        Path path = new PathMatchingResourcePatternResolver().getResource(TURF_LIBRARY).getFile().toPath();
        engine.eval(Files.newBufferedReader(path, StandardCharsets.UTF_8));
        for (int i = 0; i < 1000; i++) {
            int size = source.getCoordinates().get(0).size() - 2;
            int randomCoord = r.nextInt(1, size);
            int randomPos = r.nextInt(0, 1);
            LngLatAlt lngLatAlt = source.getCoordinates().get(0).get(randomCoord);
            if (randomPos == 0) {
                double l = lngLatAlt.getLongitude();
                lngLatAlt.setLongitude(l * 1.0001);
            } else {
                double l = lngLatAlt.getLatitude();
                lngLatAlt.setLatitude(l * 1.0001);
            }
            engine.put("data", NdJsonUtils.toJson(source.getCoordinates()));
            String res = engine.eval("JSON.stringify(turf.area(turf.polygon(JSON.parse(data))))").toString();
            log.info(res);
        }
    }

    @Test
    public void testTurfWithPureGraalEngineAndContext() throws Exception {
        Context context = Context.newBuilder("js")
                .allowHostAccess(HostAccess.NONE)
                .allowAllAccess(false)
                .allowHostClassLookup(s -> false)
                .build();

        Path jsFile = new PathMatchingResourcePatternResolver().getResource(TURF_LIBRARY).getFile().toPath();
        String turfCode = Files.readString(jsFile);
        context.eval(Source.newBuilder("js", turfCode, "turf.js").build());

        String featureJson = NdJsonUtils.toJson(getFeature());

        Value json = context.eval("js", "JSON.parse('" + featureJson + "')");
        Value coordinates = json.getMember("geometry").getMember("coordinates");
        Value turf = context.getBindings("js").getMember("turf");
        Value polygon = turf.getMember("polygon").execute(coordinates);

        Value area = turf.getMember("area").execute(polygon);

        log.info(String.valueOf(area.asDouble()));
    }

    @Test
    public void testTurfDBScanPerformance() throws Exception {
        Context context = Context.newBuilder("js")
                .allowHostAccess(HostAccess.NONE)
                .allowAllAccess(false)
                .allowHostClassLookup(s -> false)
                .build();

        Path jsFile = new PathMatchingResourcePatternResolver().getResource(TURF_LIBRARY).getFile().toPath();
        String turfCode = Files.readString(jsFile);
        context.eval(Source.newBuilder("js", turfCode, "turf.js").build());

//        let points = turf.randomPoint(10000, {bbox: [-180, -90, 180, 90]})
//        let res = JSON.stringify(turf.clustersDbscan(points, 100))

        Value bbox = context.eval("js", "JSON.parse('{\"bbox\": [-180, -90, 180, 90]}')");
        Value turf = context.getBindings("js").getMember("turf");
        Value points = turf.getMember("randomPoint").execute(100, bbox);
        log.info("start");
        Value res = turf.getMember("clustersDbscan").execute(points, 100);
        log.info("end");
    }

    private Feature getFeature() throws NdException {
        FeatureCollection features = NdJsonUtils.fromJson(param, FeatureCollection.class);
        return features.getFeatures().get(0);
    }

    private ScriptEngine graalJSScriptEngine() {
        return GraalJSScriptEngine.create(
                null,
                Context.newBuilder("js")
                        .allowHostAccess(HostAccess.NONE)
                        .allowAllAccess(false)
                        .allowHostClassLookup(s -> false));
    }

    private String param = "{\"type\":\"FeatureCollection\",\"generator\":\"overpass-ide\",\"copyright\":\"Thedataincludedinthisdocumentisfromwww.openstreetmap.org.ThedataismadeavailableunderODbL.\",\"timestamp\":\"2023-02-24T17:46:38Z\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"@id\":\"way/612169247\",\"building\":\"yes\",\"military\":\"barracks\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-104.775668,38.7362686],[-104.7756934,38.7362871],[-104.7758194,38.7361817],[-104.7758,38.7361676],[-104.77588,38.7361007],[-104.775854,38.7360818],[-104.7758628,38.7360745],[-104.775834,38.7360536],[-104.7758534,38.7360374],[-104.7758141,38.7360088],[-104.7757905,38.7360286],[-104.7757603,38.7360066],[-104.7756793,38.7360745],[-104.7757058,38.7360938],[-104.7756996,38.736099],[-104.7757226,38.7361157],[-104.7756787,38.7361525],[-104.7756499,38.7361315],[-104.7755699,38.7361984],[-104.7755979,38.7362188],[-104.7755698,38.7362424],[-104.7756391,38.7362927],[-104.775668,38.7362686]]]},\"id\":\"way/612169247\"}]}";

}
