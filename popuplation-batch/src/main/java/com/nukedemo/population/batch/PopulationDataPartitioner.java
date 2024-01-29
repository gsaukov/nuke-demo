package com.nukedemo.population.batch;

import com.nukedemo.population.services.clients.ghsl.GhslApiClient;
import lombok.extern.slf4j.Slf4j;
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
        Map<String, ExecutionContext> result = new HashMap<>();
        ExecutionContext context = new ExecutionContext();
        List<PopulationInputItem> partition = new ArrayList<>();
        PopulationInputItem item = new PopulationInputItem(maxRow, maxColumn);
        partition.add(item);
        context.put("area", new ArrayList<>(partition));
        result.put("area", context);
        return result;
    }

}
