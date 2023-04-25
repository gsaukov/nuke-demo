package com.nukedemo.core;

import com.nukedemo.core.services.utils.NdJsonUtils;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.script.ScriptEngine;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class OsmToGeoJsonServiceTest {
    @Test
    public void testOverpassClient() throws Exception {
        ScriptEngine engine = graalJSScriptEngine();
        Path path = new PathMatchingResourcePatternResolver().getResource("classpath:scripts/osmtogeojson.js").getFile().toPath();
        engine.eval(Files.newBufferedReader(path, StandardCharsets.UTF_8));
        engine.put("data", param);
        String res = engine.eval("JSON.stringify(osmtogeojson(JSON.parse(data)))").toString();
        log.info(res);
        NdJsonUtils.toJson(res);
    }

    private ScriptEngine graalJSScriptEngine() {
         return GraalJSScriptEngine.create(
                null,
                Context.newBuilder("js")
                        .allowHostAccess(HostAccess.NONE)
                        .allowAllAccess(false)
                        .allowHostClassLookup(s -> false));
    }

    private String param = "{\"version\":0.6,\"generator\":\"OverpassAPI0.7.59.436d058c8\",\"osm3s\":{\"timestamp_osm_base\":\"2023-03-13T12:11:24Z\",\"timestamp_areas_base\":\"2023-03-13T09:10:08Z\",\"copyright\":\"Thedataincludedinthisdocumentisfromwww.openstreetmap.org.ThedataismadeavailableunderODbL.\"},\"elements\":[{\"type\":\"way\",\"id\":257580170,\"center\":{\"lat\":62.3527780,\"lon\":-140.4048061},\"nodes\":[2630735400,2630735403,2630735404,2630735405,2630735406,2630735407,2630735408,2630735410,2630735412,2630735414,2630735416,2630735418,2630735419,2630735422,2630735424,2630735400],\"tags\":{\"abandoned\":\"yes\",\"aeroway\":\"aerodrome\",\"landuse\":\"military\",\"military\":\"airfield\",\"name\":\"SnagAirstrip(abandoned)\",\"note\":\"Abandonedin1967\"}},{\"type\":\"way\",\"id\":357089044,\"center\":{\"lat\":69.5957816,\"lon\":-140.1741667},\"nodes\":[3624602164,3624599785,3624602094,3624602140,3624602122,3624602153,3624602128,3624602101,3624602145,3624602154,3624602138,3624602102,3624602119,3624602126,3624602120,3624602121,3624599792,3624599790,3624602139,3624602135,3624599786,3624602164],\"tags\":{\"abandoned\":\"yes\",\"aerodrome:type\":\"abandoned\",\"aeroway\":\"aerodrome\",\"icao\":\"CYAJ\",\"landuse\":\"military\",\"military\":\"airfield\",\"name\":\"KomakukBeachAirport\",\"note\":\"Abandoned\"}},{\"type\":\"way\",\"id\":747415761,\"center\":{\"lat\":69.3327819,\"lon\":-138.7177937},\"nodes\":[6993602103,6993601711,2131542084,3625461574,3625461582,3625461583,3625461575,3625461577,3625461579,6993602103],\"tags\":{\"abandoned\":\"yes\",\"aeroway\":\"aerodrome\",\"landuse\":\"military\",\"military\":\"airfield\",\"name\":\"StokesPointSRRSAirport\"}},{\"type\":\"way\",\"id\":210606249,\"center\":{\"lat\":68.9233621,\"lon\":-137.2627706},\"nodes\":[2206832910,2206832909,2206832908,2206832911,2206832910],\"tags\":{\"landuse\":\"military\",\"name\":\"ShinglePointNWSLongRangeRadar[BAR-2]\"}},{\"type\":\"way\",\"id\":199362746,\"center\":{\"lat\":49.2753856,\"lon\":-124.2278295},\"nodes\":[2093657584,2093657589,2093657586,2093657587,2093657588,2093657585,10303804613,2093657584],\"tags\":{\"landuse\":\"military\"}},{\"type\":\"way\",\"id\":122494129,\"center\":{\"lat\":49.6626280,\"lon\":-124.9115890},\"nodes\":[1368673634,1368673606,1368673605,1368673635,1368673625,1368673611,1368673639,6530647252,6530647251,1368673656,1368673677,1368673644,1368673631,1368673597,1368673602,1368673600,1368673636,1368673665,1368673634],\"tags\":{\"building\":\"yes\",\"building:levels\":\"1\",\"landuse\":\"military\",\"military\":\"barracks\",\"name\":\"Q46\"}},{\"type\":\"way\",\"id\":906001188,\"center\":{\"lat\":49.6616674,\"lon\":-124.9155552},\"nodes\":[8413461363,8413461364,8413461365,8413461366,8413461363],\"tags\":{\"landuse\":\"military\",\"military\":\"range\",\"name\":\"QuadraAirRifleRange\"}},{\"type\":\"way\",\"id\":122494128,\"center\":{\"lat\":49.6622710,\"lon\":-124.9086879},\"nodes\":[1368673652,1368673646,1368673612,1368673664,1368673613,1368673658,1368673630,1368673624,1368673640,1368673620,1368673671,1368673673,1368673607,1368673647,1368673619,1368673604,1368673652],\"tags\":{\"building\":\"yes\",\"building:levels\":\"1\",\"landuse\":\"military\",\"military\":\"barracks\",\"name\":\"Q34\"}},{\"type\":\"way\",\"id\":122494132,\"center\":{\"lat\":49.6622563,\"lon\":-124.9096937},\"nodes\":[1368673618,1368673649,1368673627,1368673610,1368673675,1368673601,1368673603,1368673637,1368673655,1368673651,1368673608,1368673598,1368673662,1368673617,1368673642,1368673609,1368673618],\"tags\":{\"building\":\"yes\",\"building:levels\":\"1\",\"landuse\":\"military\",\"military\":\"barracks\",\"name\":\"Q35\"}},{\"type\":\"way\",\"id\":695522102,\"center\":{\"lat\":49.6622891,\"lon\":-124.9076961},\"nodes\":[6530647268,6530647267,6530647266,6530647265,6530647264,6530647263,6530647262,6530647261,6530647260,6530647259,6530647258,6530647257,6530647256,6530647255,6530647254,6530647253,6530647268],\"tags\":{\"building\":\"yes\",\"building:levels\":\"1\",\"landuse\":\"military\",\"military\":\"barracks\",\"name\":\"Q33\"}},{\"type\":\"way\",\"id\":906001184,\"center\":{\"lat\":49.6625322,\"lon\":-124.9063244},\"nodes\":[8413461335,8413461336,8413461337,8413461338,8413461339,8413461340,8413461341,8413461342,8413461343,8413461344,8413461345,8413461346,8413461335],\"tags\":{\"building\":\"yes\",\"building:levels\":\"1\",\"landuse\":\"military\",\"military\":\"barracks\",\"name\":\"Q67\"}},{\"type\":\"way\",\"id\":122490422,\"center\":{\"lat\":49.6685705,\"lon\":-124.9272230},\"nodes\":[1368661716,1368661717,1368661702,1368661661,1368661716],\"tags\":{\"landuse\":\"military\",\"man_made\":\"pier\"}},{\"type\":\"way\",\"id\":733377458,\"center\":{\"lat\":49.7036351,\"lon\":-124.8907664},\"nodes\":[6868016534,6868016535,6868016536,6868016537,6868016538,6868016539,6868016534],\"tags\":{\"landuse\":\"military\",\"name\":\"CFBComox\"}},{\"type\":\"way\",\"id\":157955086,\"center\":{\"lat\":48.3872856,\"lon\":-123.4907280},\"nodes\":[1701748879,1701748861,1701748830,1701748823,1701748791,1701748822,1701749032,1701749003,1701748879],\"tags\":{\"landuse\":\"military\",\"note\":\"obstaclecourse\"}},{\"type\":\"way\",\"id\":754356059,\"center\":{\"lat\":48.4369270,\"lon\":-123.4170788},\"nodes\":[7047467583,2010127829,6307357607,6307357651,2010127830,7047467550,7047469019,7047467570,7047469013,7047467582,7047469001,7047467579,7047467583],\"tags\":{\"landuse\":\"military\",\"source\":\"ParcelMapBCParcelFabric\"}},{\"type\":\"way\",\"id\":1087352811,\"center\":{\"lat\":48.4398479,\"lon\":-123.4155688},\"nodes\":[1700557624,1700557699,1700557697,9962732205,1700557752,9962732212,9962732211,9962732210,9962732209,9962732208,9962732207,9962732206,1700557624],\"tags\":{\"landuse\":\"military\"}}]}";

}