package com.nukedemo.core.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class GeoDataWriter implements ItemWriter<GeoDataItem> {
    @Override
    public void write(Chunk<? extends GeoDataItem> chunk) throws Exception {

    }

}
