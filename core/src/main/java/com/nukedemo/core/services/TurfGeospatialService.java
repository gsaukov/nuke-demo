package com.nukedemo.core.services;


import com.nukedemo.core.services.exceptions.NdException;
import com.nukedemo.core.services.utils.NdJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
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
    private GraalVMJsEngineService graalJSScriptEngine;

    public BigDecimal calculateArea(Polygon source) throws Exception {
        ScriptEngine engine = graalJSScriptEngine.graalJSScriptEngine(TURF_LIBRARY);
        engine.put("data", NdJsonUtils.toJson(source.getCoordinates()));
        String res = engine.eval("JSON.stringify(turf.area(turf.polygon(JSON.parse(data))))").toString();
        log.info(res);
        return new BigDecimal(res);
    }

}
