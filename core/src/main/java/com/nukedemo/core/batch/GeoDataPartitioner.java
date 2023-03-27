package com.nukedemo.core.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nukedemo.core.batch.inputmodel.BatchInput;
import com.nukedemo.core.batch.inputmodel.InputItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
        int itemsPerPartition = items.size() / gridSize + 1;
        List<List<InputItem>> partitions = ListUtils.partition(items, itemsPerPartition);
        Map<String, ExecutionContext> result = new HashMap<>();
        for (int i = 0; i < partitions.size(); i++) {
            ExecutionContext context = new ExecutionContext();
            List<InputItem> partition = partitions.get(i);
            context.put("country", new ArrayList<>(partition));
            result.put("partition_" + i + "_" + getPartitionName(partition), context);
        }
        return result;
    }

    private String getPartitionName(List<InputItem> partition) {
        if(partition.isEmpty()) {
            return "";
        }
        String first = partition.get(0).getCode();
        String last = partition.get(partition.size() - 1).getCode();
        return first + "-" + last;
    }

}
