package com.nukedemo.population.batch.layercompressionstep;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@StepScope
public class LayerCompressionDataWriter implements ItemWriter<LayerCompressionDataItem> {

    public LayerCompressionDataWriter() throws IOException {
    }

    @Override
    public void write(Chunk<? extends LayerCompressionDataItem> chunk) {
    }
}
