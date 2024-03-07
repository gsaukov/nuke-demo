package com.nukedemo.population.batch.transformerstep;

import com.fasterxml.jackson.databind.JsonNode;
import com.nukedemo.GhslMetaData;
import com.nukedemo.shared.utils.NdJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@StepScope
public class TransformerDataReader implements ItemReader<TransformerDataItem> {

    private LinkedList<TransformerInputItem> areas;

    public TransformerDataReader(@Value("#{stepExecutionContext['area']}") List<TransformerInputItem> areas) {
        this.areas = new LinkedList<>(areas);
    }

    @Override
    public TransformerDataItem read() throws IOException {
        TransformerInputItem area = areas.poll();
        if(area == null){
            return null; //Stop batch job
        }
        JsonNode root = NdJsonUtils.MAPPER.readTree(area.getFile());
        int[] intData = readIntegerArray(root);
        GhslMetaData metaData = readMetaData(root);
        return new TransformerDataItem(metaData, intData);
    }

    private int[] readIntegerArray(JsonNode root) throws IOException {
        JsonNode node = root.get("data").get("res");
        return NdJsonUtils.MAPPER.treeToValue(node, int[].class);
    }

    private GhslMetaData readMetaData(JsonNode root) throws IOException {
        JsonNode node = root.get("metadata");
        return NdJsonUtils.MAPPER.treeToValue(node, GhslMetaData.class);
    }

}
