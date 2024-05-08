package com.nukedemo.population.batch.shared;

import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.nukedemo.GhslMetaData;
import com.nukedemo.shared.utils.NdJsonUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.batch.item.ExecutionContext;

import java.io.File;
import java.io.IOException;
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

    public static String getGhslKey(String resolution, int row, int column) {
        return "GHS_POP_E2025_GLOBE_R2023A_" + resolution + "_V1_0_R" + row + "_C" + column;
    }

    public static Map<String, GhslMetaData> getMetaData(String path) throws IOException {
        File metaDataFile = new File(path, "metaData.json");
        MapType ref = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, GhslMetaData.class);
        return NdJsonUtils.MAPPER.readValue(metaDataFile, ref);
    }

}
