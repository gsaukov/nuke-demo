package com.nukedemo.geocalculator.services;


import com.mapbox.geojson.FeatureCollection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;

@Slf4j
@Service
public class OsmToGeoJsonService {

    private static final String OSMTOGEOJSON_LIBRARY = "scripts/osmtogeojson.js";
    private GraalVMJSScriptingEngineService engine;

    public OsmToGeoJsonService(GraalVMJSScriptingEngineService graalJSScriptingEngine) throws ScriptException, IOException {
        this.engine = graalJSScriptingEngine;
        this.engine.registerPathResource(OSMTOGEOJSON_LIBRARY);
    }

    public String convert(String source) throws Exception {
        engine.getScriptEngine().put("data", source);
        String res = engine.getScriptEngine().eval("JSON.stringify(osmtogeojson(JSON.parse(data)))").toString();
//        log.info(res);
        return res;
    }

    private FeatureCollection convertOSMtoGeoJSON(String source) {
        return FeatureCollection.fromJson(source);
    }

}
