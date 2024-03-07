package com.nukedemo.population.batch.populationstep;

import com.nukedemo.population.batch.shared.BatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@StepScope
@Service
public class PopulationDataPartitioner implements Partitioner {

    @Value("${populationBatch.ghsl.skipProcessing}")
    private boolean skipProcessing;


    @Value("${populationBatch.ghsl.resultFolder}")
    String resultFolder;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize)  {
        if(skipProcessing) {
            return BatchUtils.executions(1, new ArrayList<>());
        } else {
            return BatchUtils.executions(gridSize, getItems());
        }
    }

    private List<PopulationInputItem> getItems(){
        List<PopulationInputItem> items = null;
        try {
            items = getInputItems();
        } catch (IOException e) {
            throw new RuntimeException("Partitioning failed", e);
        }
        return items;
    }

    private List<PopulationInputItem> getInputItems() throws IOException {
        String folderPath = resultFolder;
        String filePattern = folderPath + "/*.zip";

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("file:" + filePattern);
        List<PopulationInputItem> populationInputItems = new ArrayList<>();
        for (Resource resource : resources) {
            File file = resource.getFile();
            if (file.exists() && file.isFile()) {
                populationInputItems.add(new PopulationInputItem(file));
            }
        }
        return populationInputItems;
    }

}
