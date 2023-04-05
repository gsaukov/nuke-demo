package com.nukedemo.core.services;


import com.nukedemo.core.services.utils.NdJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.geojson.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import java.math.BigDecimal;

@Slf4j
@Service
public class TurfGeospatialService {

    private static final String TURF_LIBRARY = "classpath:scripts/turf.js";

    @Autowired
    private GraalVMJSScriptingEngineService graalJSScriptingEngine;

    public BigDecimal calculateArea(Polygon source) throws Exception {
        ScriptEngine engine = graalJSScriptingEngine.graalJSScriptEngine(TURF_LIBRARY);
        engine.put("data", NdJsonUtils.toJson(source.getCoordinates()));
        String res = engine.eval("JSON.stringify(turf.area(turf.polygon(JSON.parse(data))))").toString();
        log.info(res);
        return new BigDecimal(res);
    }

}
