package com.nukedemo.core.services;


import com.nukedemo.core.services.exceptions.NdException;
import com.nukedemo.core.services.utils.NdJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;

@Slf4j
@Service
public class OsmToGeoJsonConverter {

    private static final String OSMTOGEOJSON_LIBRARY = "classpath:scripts/osmtogeojson.js";

    @Autowired
    private GraalVMJsEngineService graalJSScriptEngine;

    public FeatureCollection convert(String source) throws Exception {
        ScriptEngine engine = graalJSScriptEngine.graalJSScriptEngine(OSMTOGEOJSON_LIBRARY);
        engine.put("data", source);
        String res = engine.eval("JSON.stringify(osmtogeojson(JSON.parse(data)))").toString();
        log.info(res);
        return convertOSMtoGeoJSON(res);
    }

    private FeatureCollection convertOSMtoGeoJSON(String source) throws NdException {
        return NdJsonUtils.fromJson(source, FeatureCollection.class);
    }

}
