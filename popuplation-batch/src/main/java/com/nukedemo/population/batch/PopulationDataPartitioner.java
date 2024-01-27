package com.nukedemo.population.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PopulationDataPartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> result = new HashMap<>();
            ExecutionContext context = new ExecutionContext();
            List<PopulationInputItem> partition = new ArrayList<>();
            PopulationInputItem item = new PopulationInputItem();
            item.setAreaCode("someArea");
            partition.add(item);
            context.put("area", new ArrayList<>(partition));
            result.put("area", context);
        return result;
    }

}
