package com.nukedemo.core.services;


import com.nukedemo.core.services.exceptions.NdException;
import com.nukedemo.core.services.utils.NdJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;

@Slf4j
@Service
@StepScope
public class OsmToGeoJsonService {

    private static final String OSMTOGEOJSON_LIBRARY = "classpath:scripts/osmtogeojson.js";
    private GraalVMJSScriptingEngineService engine;

    public OsmToGeoJsonService(GraalVMJSScriptingEngineService graalJSScriptingEngine) throws ScriptException, IOException {
        this.engine = graalJSScriptingEngine;
        this.engine.registerPathResource(OSMTOGEOJSON_LIBRARY);
    }

    public FeatureCollection convert(String source) throws Exception {
        engine.getScriptEngine().put("data", source);
        String res = engine.getScriptEngine().eval("JSON.stringify(osmtogeojson(JSON.parse(data)))").toString();
        log.info(res);
        return convertOSMtoGeoJSON(res);
    }

    private FeatureCollection convertOSMtoGeoJSON(String source) throws NdException {
        return NdJsonUtils.fromJson(source, FeatureCollection.class);
    }

}