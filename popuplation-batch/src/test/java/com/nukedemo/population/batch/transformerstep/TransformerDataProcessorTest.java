package com.nukedemo.population.batch.transformerstep;

import com.fasterxml.jackson.databind.JsonNode;
import com.nukedemo.GhslMetaData;
import com.nukedemo.geocalculator.services.PolygonClippingService;
import com.nukedemo.population.PopulationBatchApplication;
import com.nukedemo.shared.utils.NdJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TransformerDataProcessorTest {


    TransformerDataProcessor processor ;

    @BeforeEach
    void setUp() {
        processor = new TransformerDataProcessor(new PolygonClippingService());
    }

    @Test
    public void testTransformation() throws Exception {
//        File dataFile = new PathMatchingResourcePatternResolver().getResource("GHS_POP_E2025_GLOBE_R2023A_4326_30ss_V1_0_R4_C20_int.json").getFile();
//        processor.process(getData(dataFile));
    }

    private TransformerDataItem getData(File file) throws IOException {
        JsonNode root = NdJsonUtils.MAPPER.readTree(file);
        int[] intData = readIntegerArray(root);
        GhslMetaData metaData = readMetaData(root);
        return new TransformerDataItem(metaData, intData);
    }

    private int[] readIntegerArray(JsonNode root) throws IOException {
        JsonNode node = root.get("data").get("res");
        return NdJsonUtils.MAPPER.treeToValue(node, int[].class);
    }

    private GhslMetaData readMetaData(JsonNode root) throws IOException {
        JsonNode node = root.get("metaData");
        return NdJsonUtils.MAPPER.treeToValue(node, GhslMetaData.class);
    }
}