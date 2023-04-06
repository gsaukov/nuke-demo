package com.nukedemo.core;

import com.nukedemo.core.services.utils.NdJsonUtils;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.Polygon;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.junit.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.script.ScriptEngine;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

@Slf4j
public class TurfFunctionsTest {

    {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
    }

    private static final String TURF_LIBRARY = "classpath:scripts/turf.js";

    @Test
    public void testTurf() throws Exception {
        ScriptEngine engine = graalJSScriptEngine();
        Path path = new PathMatchingResourcePatternResolver().getResource("classpath:scripts/turf.js").getFile().toPath();
        engine.eval(Files.newBufferedReader(path, StandardCharsets.UTF_8));
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
    public void testTurfWithPolygon() throws Exception {
        FeatureCollection features = NdJsonUtils.fromJson(param, FeatureCollection.class);
        Polygon source = (Polygon)features.getFeatures().get(0).getGeometry();
        ScriptEngine engine = graalJSScriptEngine();
        Path path = new PathMatchingResourcePatternResolver().getResource("classpath:scripts/turf.js").getFile().toPath();
        engine.eval(Files.newBufferedReader(path, StandardCharsets.UTF_8));
        engine.put("data", NdJsonUtils.toJson(source.getCoordinates()));
        String res = engine.eval("JSON.stringify(turf.area(turf.polygon(JSON.parse(data))))").toString();
        log.info(res);
    }

    @Test
    public void testTurfOnRandomCoordinatesReuseEngine() throws Exception {
        Random r = new Random();
        FeatureCollection features = NdJsonUtils.fromJson(param, FeatureCollection.class);
        Polygon source = (Polygon)features.getFeatures().get(0).getGeometry();
        ScriptEngine engine = graalJSScriptEngine();
        Path path = new PathMatchingResourcePatternResolver().getResource(TURF_LIBRARY).getFile().toPath();
        engine.eval(Files.newBufferedReader(path, StandardCharsets.UTF_8));
        for(int i = 0; i < 1000; i++) {
            int size = source.getCoordinates().get(0).size() - 2;
            int randomCoord = r.nextInt(1, size);
            int randomPos = r.nextInt(0, 1);
            LngLatAlt lngLatAlt = source.getCoordinates().get(0).get(randomCoord);
            if(randomPos==0){
                double l = lngLatAlt.getLongitude();
                lngLatAlt.setLongitude(l*1.0001);
            } else {
                double l = lngLatAlt.getLatitude();
                lngLatAlt.setLatitude(l*1.0001);
            }
            engine.put("data", NdJsonUtils.toJson(source.getCoordinates()));
            String res = engine.eval("JSON.stringify(turf.area(turf.polygon(JSON.parse(data))))").toString();
            log.info(res);
        }
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