package com.nukedemo.population.batch.layercompressionstep;

import com.nukedemo.population.batch.shared.BatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nukedemo.population.batch.layercompressionstep.LayerCompressionStepConfiguration.SOURCE_PATH;

@Slf4j
@StepScope
@Service
public class LayerCompressionDataPartitioner implements Partitioner {

    private static final String RESOLUTION = "4326_90ss";
    public static final String ORIGINAL_RESOLUTION = "4326_30ss";
    public static final String DUMMY = "DUMMY";
    private static final int BLOCK_SIZE = 3;

    @Value("${populationBatch.ghsl.maxRow}")
    private int maxRow;

    @Value("${populationBatch.ghsl.maxColumn}")
    private int maxColumn;

    public LayerCompressionDataPartitioner() {
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        List<LayerCompressionInputItem> items = getItems();
        return BatchUtils.executions(gridSize, items);
    }

    private List<LayerCompressionInputItem> getItems() {
        List<LayerCompressionInputItem> items = null;
        try {
            items = getInputItems();
        } catch (IOException e) {
            throw new RuntimeException("Partitioning failed", e);
        }
        return items;
    }

    private List<LayerCompressionInputItem> getInputItems() throws IOException {
        List<LayerCompressionInputItem> items = new ArrayList<>();
        for (int row = 1; row < maxRow; row = row + BLOCK_SIZE) {
            for (int column = 1; column < maxColumn; column = column + BLOCK_SIZE) {
                List<List<String>> block = getBlock(row, column);
                if(block != null){
                    LayerCompressionInputItem item = new LayerCompressionInputItem(block);
                    items.add(item);
                }
            }
        }
        return items;
    }

    private List<List<String>> getBlock(int rowStart, int columnStart) {
        boolean hasData = false;
        List<List<String>> block = new ArrayList<>();
        for (int row = rowStart; row < rowStart + BLOCK_SIZE; row++) {
            List<String> rowBlock = new ArrayList<>();
            for (int column = columnStart; column < columnStart + BLOCK_SIZE; column++) {
                if (exists(row, column)) {
                    rowBlock.add(getGhslKey(row, column));
                    hasData = true;
                } else {
                    rowBlock.add(DUMMY);
                }
            }
            block.add(rowBlock);
        }
        return hasData ? block : null;
    }


    private boolean exists(int row, int column) {
        String fileName = getGhslKey(row, column) + ".png";
        return new File(SOURCE_PATH + "/" + ORIGINAL_RESOLUTION + "/img/", fileName).exists();
    }

    private String getGhslKey(int row, int column) {
        return "GHS_POP_E2025_GLOBE_R2023A_" + ORIGINAL_RESOLUTION + "_V1_0_R" + row + "_C" + column;
    }


    // [
    //  [DUMMY,                                              DUMMY,                                              DUMMY],
    //  [GHS_POP_E2025_GLOBE_R2023A_4326_30ss_V1_0_R2_C1,    GHS_POP_E2025_GLOBE_R2023A_4326_30ss_V1_0_R2_C2,    GHS_POP_E2025_GLOBE_R2023A_4326_30ss_V1_0_R2_C3],
    //  [GHS_POP_E2025_GLOBE_R2023A_4326_30ss_V1_0_R3_C1,    GHS_POP_E2025_GLOBE_R2023A_4326_30ss_V1_0_R3_C2,    GHS_POP_E2025_GLOBE_R2023A_4326_30ss_V1_0_R3_C3]
    // ]

}
