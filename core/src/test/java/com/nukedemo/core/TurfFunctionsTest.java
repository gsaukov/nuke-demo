package com.nukedemo.core;

import com.nukedemo.shared.utils.NdJsonUtils;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
import org.geojson.Polygon;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class TurfFunctionsTest {

    {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
    }

    private static final String TURF_LIBRARY = "classpath:scripts/turf.js";

    private ScriptEngine engine;

    @BeforeEach
    public void setUp() throws IOException, ScriptException {
        engine = graalJSScriptEngine();
        Path path = new PathMatchingResourcePatternResolver().getResource(TURF_LIBRARY).getFile().toPath();
        engine.eval(Files.newBufferedReader(path, StandardCharsets.UTF_8));
    }

    @Test
    public void testTurf() throws Exception {
        String res = engine.eval("JSON.stringify(turf.area(turf.polygon([[[125, -15], [113, -22], [154, -27], [144, -15], [125, -15]]])))").toString();
        log.info(res);
        res = engine.eval("JSON.stringify(turf.area(turf.polygon([[[125, -13], [113, -20], [154, -27], [144, -15], [125, -13]]])))").toString();
        log.info(res);
        res = engine.eval("JSON.stringify(turf.area(turf.polygon([[[125, -11], [113, -22], [154, -27], [144, -3], [125, -11]]])))").toString();
        log.info(res);
        res = engine.eval("JSON.stringify(turf.area(turf.polygon([[[125, -14], [100, -22], [154, -27], [144, -3], [125, -14]]])))").toString();
        log.info(res);
        res = engine.eval("JSON.stringify(turf.area(turf.polygon([[[-104.775668,38.7362686],[-104.7756934,38.7362871],[-104.7758194,38.7361817],[-104.7758,38.7361676],[-104.77588,38.7361007],[-104.775854,38.7360818],[-104.7758628,38.7360745],[-104.775834,38.7360536],[-104.7758534,38.7360374],[-104.7758141,38.7360088],[-104.7757905,38.7360286],[-104.7757603,38.7360066],[-104.7756793,38.7360745],[-104.7757058,38.7360938],[-104.7756996,38.736099],[-104.7757226,38.7361157],[-104.7756787,38.7361525],[-104.7756499,38.7361315],[-104.7755699,38.7361984],[-104.7755979,38.7362188],[-104.7755698,38.7362424],[-104.7756391,38.7362927],[-104.775668,38.7362686]]])))").toString();
        log.info(res);
    }

    @Test
    public void testTurfAreaWithPolygon() throws Exception {
        FeatureCollection features = NdJsonUtils.fromJson(param, FeatureCollection.class);
        Polygon source = (Polygon) features.getFeatures().get(0).getGeometry();
        engine.put("data", NdJsonUtils.toJson(source.getCoordinates()));
        String res = engine.eval("JSON.stringify(turf.area(turf.polygon(JSON.parse(data))))").toString();
        log.info(res);
    }

    // O(n^2) Calculation aproximation on Mac M1
    // 1000 - 6sec;
    // 10000 ~ 600sec, 10 minutes
    // 100000 ~ 60000sec, 1000 minutes, ~17 hours.
    // JS execution in turf_test.html 10000 - 12 seconds.
    @Test
    public void testTurfClustersDbScan() throws Exception {
//        Path path = new PathMatchingResourcePatternResolver().getResource("file:../data/mil/canada_cropped.geojson").getFile().toPath();
//        String data = Files.readString(path, StandardCharsets.UTF_8);
        String data = engine.eval("JSON.stringify(turf.randomPoint(100, {bbox: [-180, -90, 180, 90]}))").toString();
        engine.put("data", data);
        log.info("start");
        String res = engine.eval("JSON.stringify(turf.clustersDbscan(JSON.parse(data), 100))").toString();
        log.info("end");
        log.info(res);
    }

    public void testTurfCentroid() throws Exception {
//        Path path = new PathMatchingResourcePatternResolver().getResource("file:../data/mil/canada_cropped.geojson").getFile().toPath();
//        String data = Files.readString(path, StandardCharsets.UTF_8);
        String data = engine.eval("JSON.stringify(turf.randomPoint(100, {bbox: [-180, -90, 180, 90]}))").toString();
        engine.put("data", data);
        log.info("start");
        String res = engine.eval("JSON.stringify(turf.clustersDbscan(JSON.parse(data), 100))").toString();
        log.info("end");
        log.info(res);
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