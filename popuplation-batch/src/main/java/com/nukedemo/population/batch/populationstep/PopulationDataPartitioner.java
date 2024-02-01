package com.nukedemo.population.batch.populationstep;

import com.nukedemo.population.services.clients.ghsl.GhslApiClient;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@StepScope
@Service
public class PopulationDataPartitioner implements Partitioner {

    @Value("${populationBatch.ghsl.maxRow}")
    private int maxRow;

    @Value("${populationBatch.ghsl.maxColumn}")
    private int maxColumn;

    @Autowired
    GhslApiClient ghslApiClient;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        List<PopulationInputItem> items = getItems();
        return executions(gridSize, items);
    }

    private List<PopulationInputItem> getItems() {
        List<PopulationInputItem> items = new ArrayList<>();
        for (int row = 1; row <= maxRow; row++) {
            for (int column = 1; column <= maxColumn; column++) {
                if (checkFileExists(row, column)) {
                    items.add(new PopulationInputItem(row, column));
                }
            }
        }
        return items;
    }

    private Map<String, ExecutionContext> executions(int gridSize, List<PopulationInputItem> items) {
        int itemsPerPartition = items.size() / gridSize + 1;
        List<List<PopulationInputItem>> partitions = ListUtils.partition(items, itemsPerPartition);
        Map<String, ExecutionContext> result = new HashMap<>();
        for (int i = 0; i < partitions.size(); i++) {
            ExecutionContext context = new ExecutionContext();
            List<PopulationInputItem> partition = partitions.get(i);
            context.put("area", new ArrayList<>(partition));
            result.put("partition_" + i, context);
        }
        return result;
    }

    private boolean checkFileExists(int row, int column) {
        Response fileExists = ghslApiClient.checkFileExists(row, column);
        return fileExists.status() == 200;
    }

}
