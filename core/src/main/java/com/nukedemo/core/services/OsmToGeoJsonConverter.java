package com.nukedemo.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class OsmToGeoJsonConverter {

    //ONLY WORKS WITH GRAALVM OTHERWISE JS ENGINE WILL BE NULL!
    //    implementation 'org.graalvm.js:js:22.3.1'
    //    implementation 'org.graalvm.js:js-scriptengine:22.3.1'

    private static final String OSMTOGEOJSON_LS_LIBRARY = "classpath:scripts/osmtogeojson.js";

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public FeatureCollection convert(String source) throws Exception {
        ScriptEngine engine = graalJSScriptEngine();
        engine.put("data", source);
        String res = engine.eval("JSON.stringify(osmtogeojson(JSON.parse(data)))").toString();
        log.info(res);
        return convertOSMtoGeoJSON(res);
    }

    private ScriptEngine graalJSScriptEngine() throws IOException, ScriptException {
        ScriptEngine engine = GraalJSScriptEngine.create(
                null,
                Context.newBuilder("js")
                        .allowHostAccess(HostAccess.NONE)
                        .allowAllAccess(false)
                        .allowHostClassLookup(s -> false));
        Path path = new PathMatchingResourcePatternResolver().getResource(OSMTOGEOJSON_LS_LIBRARY).getFile().toPath();
        engine.eval(Files.newBufferedReader(path, StandardCharsets.UTF_8));
        return engine;
    }

    private FeatureCollection convertOSMtoGeoJSON(String source) {
        FeatureCollection sample = null;
        try {
            sample = mapper.readValue(source, FeatureCollection.class);
        } catch (IOException e) {
            log.error("Deserialization failed", e);
        }
        return sample;
    }

}
