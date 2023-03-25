package com.nukedemo.core.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nukedemo.core.batch.inputmodel.BatchInput;
import com.nukedemo.core.batch.inputmodel.InputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GeoDataPartitioner implements Partitioner {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        BatchInput inputModel = deserializeInputModel("classpath:inputdata/parties.json");
        return executions(gridSize, inputModel.getInput());
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

    private Map<String, ExecutionContext> executions(int gridSize, List<InputItem> items) {
//        int itemsPerPartition = items.size() / gridSize + 1;
        Map<String, ExecutionContext> result = new HashMap<>();
        for (int i = 0; i<items.size(); i++) {
            ExecutionContext context = new ExecutionContext();
            context.put("country", items.get(i));
            result.put("partition_" + i, context);
        }
        return result;
    }
}
