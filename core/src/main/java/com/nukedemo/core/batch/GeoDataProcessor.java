package com.nukedemo.core.batch;

import com.nukedemo.core.services.GraalVMJSScriptingEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

@Slf4j
public class GeoDataProcessor implements ItemProcessor<String, GeoDataItem> {

    private UUID uuid = UUID.randomUUID();

    private GraalVMJSScriptingEngineService service;

    public GeoDataProcessor(GraalVMJSScriptingEngineService service) {
        this.service = service;
    }

    @Override
    public GeoDataItem process(String item) {
        log.info("Processor ID: " + uuid + " Item: " + item);
        //process/Convert GeorJson items from nominatim
        return new GeoDataItem(item);
    }
}
