package com.nukedemo.core.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nukedemo.core.batch.inputmodel.BatchInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
public class GeoDataPartitioner implements Partitioner {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        BatchInput inputModel = deserializeInputModel("classpath:inputdata/parties.json");
        return null;
    }

    public BatchInput deserializeInputModel(String location) {
        BatchInput model = null;
        try {
            InputStream inJson = new PathMatchingResourcePatternResolver()
                    .getResource(location).getInputStream();
            model = mapper.readValue(inJson, BatchInput.class);
        } catch (IOException e) {
            log.error("deserialization failed", e);
        }
        return model;
    }
}
