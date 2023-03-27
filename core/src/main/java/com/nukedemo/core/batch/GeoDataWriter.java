package com.nukedemo.core.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.UUID;

@Slf4j
public class GeoDataWriter implements ItemWriter<GeoDataItem> {

    private UUID uuid = UUID.randomUUID();

    @Override
    public void write(Chunk<? extends GeoDataItem> chunk) throws Exception {
        //write chunks of objects to documents
        for(GeoDataItem item : chunk.getItems()){
            log.info("Writer ID:" + uuid + " Item: " + item.getCountry());
        }
    }

}
