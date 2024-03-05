package com.nukedemo.population.batch.ghslstep;

import com.nukedemo.population.batch.shared.BatchUtils;
import com.nukedemo.population.services.clients.ghsl.GhslApiClient;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@StepScope
@Service
public class GhslFileDataPartitioner implements Partitioner {

    @Value("${populationBatch.ghsl.skipDownload}")
    private boolean skipDownload;

    @Value("${populationBatch.ghsl.overwriteFiles}")
    private boolean overwriteFiles;

    @Value("${populationBatch.ghsl.resolution}")
    private String resolution;

    @Value("${populationBatch.ghsl.resultFolder}")
    String resultFolder;

    @Value("${populationBatch.ghsl.maxRow}")
    private int maxRow;

    @Value("${populationBatch.ghsl.maxColumn}")
    private int maxColumn;

    @Autowired
    GhslApiClient ghslApiClient;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        if(skipDownload) {
            return BatchUtils.executions(1, new ArrayList<>());
        } else {
            return BatchUtils.executions(gridSize, getItems());
        }
    }

    private List<GhslFileInputItem> getItems() {
        List<GhslFileInputItem> items = new ArrayList<>();
        for (int row = 1; row <= maxRow; row++) {
            for (int column = 1; column <= maxColumn; column++) {
                if (checkFileExists(row, column) && shouldDownload(row, column)) {
                    items.add(new GhslFileInputItem(resolution, row, column));
                }
            }
        }
        return items;
    }

    private boolean checkFileExists(int row, int column) {
        try {
            Response fileExists = ghslApiClient.checkFileExists(resolution, row, column);
            return fileExists.status() == 200;
        } catch (Exception e) {
            log.error("Error checking: R" + row + "_C" + column);
            return false;
        }

    }

    private boolean shouldDownload(int row, int column) {
        if(overwriteFiles) {
            return overwriteFiles;
        } else {
            return !(new File(resultFolder,  "R" + row+ "_C" + column + ".zip").exists());
        }
    }

}
