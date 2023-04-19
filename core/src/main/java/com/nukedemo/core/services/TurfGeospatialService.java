package com.nukedemo.core.services;


import com.nukedemo.core.services.utils.NdJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.geojson.Polygon;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
@StepScope
public class TurfGeospatialService {

    private static final String TURF_LIBRARY = "classpath:scripts/turf.js";

    private GraalVMJSScriptingEngineService engine;

    public TurfGeospatialService(GraalVMJSScriptingEngineService graalJSScriptingEngine) throws ScriptException, IOException {
        this.engine = graalJSScriptingEngine;
        this.engine.registerPathResource(TURF_LIBRARY);
        log.info("Creating for thread: "+ Thread.currentThread().getId());
    }

    public BigDecimal calculateArea(Polygon source) throws Exception {
        engine.getScriptEngine().put("data", NdJsonUtils.toJson(source.getCoordinates()));
        String res = engine.getScriptEngine().eval("JSON.stringify(turf.area(turf.polygon(JSON.parse(data))))").toString();
        log.info(res);
        return new BigDecimal(res);
    }

}
