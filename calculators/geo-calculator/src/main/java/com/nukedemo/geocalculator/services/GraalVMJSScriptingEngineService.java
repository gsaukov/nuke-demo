package com.nukedemo.geocalculator.services;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
        Path path = new PathMatchingResourcePatternResolver().getResource(resource).getFile().toPath();
        scriptEngine.eval(Files.newBufferedReader(path, StandardCharsets.UTF_8));
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
