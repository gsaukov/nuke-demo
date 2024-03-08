package com.nukedemo.geocalculator.services;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class GraalVMJSScriptingEngineService {

    static {
        /*
         * Disables redundant warning in log.
         */

        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
    }

    private final GraalJSScriptEngine scriptEngine;

    public GraalVMJSScriptingEngineService() {
        this.scriptEngine = createGraalJSScriptEngine();
    }

    public void registerPathResource(String resource) throws IOException, ScriptException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resource);
        scriptEngine.eval(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    public GraalJSScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    private GraalJSScriptEngine createGraalJSScriptEngine() {
        return GraalJSScriptEngine.create(
                null,
                Context.newBuilder("js")
                        .allowHostAccess(HostAccess.NONE)
                        .allowAllAccess(false)
                        .allowHostClassLookup(s -> false));
    }

}
