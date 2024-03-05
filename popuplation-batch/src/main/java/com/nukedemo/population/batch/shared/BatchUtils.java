package com.nukedemo.population.batch.shared;

import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ExecutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchUtils {

    public static <T> Map<String, ExecutionContext> executions(int gridSize, List<T> items) {
        int itemsPerPartition = items.size() / gridSize + 1;
        List<List<T>> partitions = ListUtils.partition(items, itemsPerPartition);
        Map<String, ExecutionContext> result = new HashMap<>();
        for (int i = 0; i < partitions.size(); i++) {
            ExecutionContext context = new ExecutionContext();
            List<T> partition = partitions.get(i);
            context.put("area", new ArrayList<>(partition));
            result.put("partition_" + i, context);
        }
        return result;
    }

}
