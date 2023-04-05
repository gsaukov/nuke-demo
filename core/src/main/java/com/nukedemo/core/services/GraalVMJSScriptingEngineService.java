package com.nukedemo.core.services;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.extern.slf4j.Slf4j;
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
/**
 * @deprecated use {@link GraalVMJSEngineService}
 */
@Deprecated
public class GraalVMJSScriptingEngineService {

    public GraalVMJSScriptingEngineService() {
        /*
        * Disables redundant warning in log.
         */
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
    }

    /*
     * 1. In a polyglot application, an arbitrary number of JS runtimes can be created, but they should be used by one thread at a time.
     * 2. Concurrent access to Java objects is allowed: any Java object can be accessed by any Java or JavaScript thread, concurrently.
     * 3. Concurrent access to JavaScript objects is not allowed: any JavaScript object cannot be accessed by more than one thread at a time.
     * https://medium.com/graalvm/multi-threaded-java-javascript-language-interoperability-in-graalvm-2f19c1f9c37b
     * https://www.graalvm.org/22.0/reference-manual/embed-languages/#code-caching-across-multiple-contexts
     *
     * Now new GraalJSScriptEngine is created for every request to JS scripting engine.
     * GraalJSScriptEngine vs Context implementation details: https://www.graalvm.org/latest/reference-manual/js/ScriptEngine/
     * [TODO] Create multiple, isolated, JS runtimes by either session scope beans to exclude simultaneous access, or pool of engines that are returned to pool via java.io.Closeable
     * [TODO] Migrate to Graalvm context for more fine grained execution features.
     */
    public ScriptEngine graalJSScriptEngine() {
        return GraalJSScriptEngine.create(
                null,
                Context.newBuilder("js")
                        .allowHostAccess(HostAccess.NONE)
                        .allowAllAccess(false)
                        .allowHostClassLookup(s -> false));
    }

    public ScriptEngine graalJSScriptEngine(String resource) throws IOException, ScriptException {
        ScriptEngine engine = graalJSScriptEngine();
        Path path = new PathMatchingResourcePatternResolver().getResource(resource).getFile().toPath();
        engine.eval(Files.newBufferedReader(path, StandardCharsets.UTF_8));
        return engine;
    }

}
