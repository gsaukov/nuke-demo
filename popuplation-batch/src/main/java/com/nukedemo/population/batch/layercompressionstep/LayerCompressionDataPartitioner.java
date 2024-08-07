package com.nukedemo.population.batch.layercompressionstep;

import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.nukedemo.GhslMetaData;
import com.nukedemo.population.batch.shared.BatchUtils;
import com.nukedemo.shared.utils.NdJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.nukedemo.population.batch.layercompressionstep.LayerCompressionStepConfiguration.ORIGINAL_RESOLUTION;
import static com.nukedemo.population.batch.populationstep.PopulationDataWriter.POPULATION_IMG_FOLDER;
import static com.nukedemo.population.batch.shared.BatchUtils.getGhslKey;
import static com.nukedemo.population.batch.shared.BatchUtils.getMetaData;

@Slf4j
@StepScope
@Service
public class LayerCompressionDataPartitioner implements Partitioner {

    public static final String DUMMY = "DUMMY";
    private static final int BLOCK_SIZE = 3;

    @Value("${populationBatch.ghsl.basePath}")
    private String basePath;

    @Value("${populationBatch.ghsl.maxRow}")
    private int maxRow;

    @Value("${populationBatch.ghsl.maxColumn}")
    private int maxColumn;

    private List<Double> topLeftLons;
    private List<Double> topLeftLats;
    private List<Double> bottomRightLons;
    private List<Double> bottomRightLats;

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
        Map<String, GhslMetaData> meta = getMetaData(basePath + "/" + ORIGINAL_RESOLUTION + "/");
        calculateGrid(meta);
        List<LayerCompressionInputItem> items = new ArrayList<>();
        int compressedRow = 1;
        int compressedColumn = 1;
        for (int row = 1; row < maxRow; row = row + BLOCK_SIZE) {
            for (int column = 1; column < maxColumn; column = column + BLOCK_SIZE) {
                List<List<String>> block = getBlock(row, column);
                if(block != null){
                    double [][] blockDimensions = calculateBlockDimensions(row, column);
                    LayerCompressionInputItem item = new LayerCompressionInputItem(compressedRow, compressedColumn, block, blockDimensions);
                    items.add(item);
                }
                compressedColumn++; //this increases compressed column number that does not match original
            }
            compressedColumn = 1; //reset on a new row
            compressedRow++; //this increases compressed row number that does not match original
        }
        return items;
    }

    private double[][] calculateBlockDimensions(int row, int column) {
        int lonIndex = column - 1;
        int latIndex = row - 1;
        int step = BLOCK_SIZE - 1;
        return new double[][] {
                {topLeftLons.get(lonIndex), topLeftLats.get(latIndex)},
                {bottomRightLons.get(lonIndex + step), bottomRightLats.get(latIndex + step)}
        };
    }

    private void calculateGrid(Map<String, GhslMetaData> meta) {
        TreeSet<Double> sortedTopLeftLons = new TreeSet<>();
        TreeSet<Double> sortedTopLeftLats = new TreeSet<>();
        TreeSet<Double> sortedBottomRightLons = new TreeSet<>();
        TreeSet<Double> sortedBottomRightLats = new TreeSet<>();
        for (GhslMetaData val :meta.values()) {
            sortedTopLeftLons.add(val.getTopLeftCorner()[0]);
            sortedTopLeftLats.add(val.getTopLeftCorner()[1]);
            sortedBottomRightLons.add(val.getBottomRightCorner()[0]);
            sortedBottomRightLats.add(val.getBottomRightCorner()[1]);
        }
        topLeftLons = new ArrayList<>(sortedTopLeftLons);
        topLeftLats = new ArrayList<>(sortedTopLeftLats);
        Collections.sort(topLeftLats, Collections.reverseOrder());
        bottomRightLons = new ArrayList<>(sortedBottomRightLons);
        bottomRightLats = new ArrayList<>(sortedBottomRightLats);
        Collections.sort(bottomRightLats, Collections.reverseOrder());
    }

    private List<List<String>> getBlock(int rowStart, int columnStart) {
        boolean hasData = false;
        List<List<String>> block = new ArrayList<>();
        for (int row = rowStart; row < rowStart + BLOCK_SIZE; row++) {
            List<String> rowBlock = new ArrayList<>();
            for (int column = columnStart; column < columnStart + BLOCK_SIZE; column++) {
                if (exists(row, column)) {
                    rowBlock.add(getGhslKey(ORIGINAL_RESOLUTION, row, column));
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
        String fileName = getGhslKey(ORIGINAL_RESOLUTION, row, column) + ".png";
        return new File(basePath + "/" + ORIGINAL_RESOLUTION + POPULATION_IMG_FOLDER, fileName).exists();
    }

}
