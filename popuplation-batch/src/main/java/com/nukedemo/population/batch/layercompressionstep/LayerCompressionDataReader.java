package com.nukedemo.population.batch.layercompressionstep;

import com.nukedemo.ImageTransformations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.nukedemo.population.batch.layercompressionstep.LayerCompressionDataPartitioner.DUMMY;
import static com.nukedemo.population.batch.layercompressionstep.LayerCompressionStepConfiguration.ORIGINAL_RESOLUTION;
import static com.nukedemo.population.batch.populationstep.PopulationDataWriter.POPULATION_IMG_FOLDER;

@Slf4j
@Service
@StepScope
public class LayerCompressionDataReader implements ItemReader<LayerCompressionDataItem> {

    @Value("${populationBatch.ghsl.basePath}")
    private String basePath;

    private static final Map<String, BufferedImage> MAP = new ConcurrentHashMap<>();

    private LinkedList<LayerCompressionInputItem> areas;

    public LayerCompressionDataReader(@Value("#{stepExecutionContext['area']}") List<LayerCompressionInputItem> areas) {
        this.areas = new LinkedList<>(areas);
    }

    @Override
    public LayerCompressionDataItem read() throws IOException {
        LayerCompressionInputItem area = areas.poll();
        if (area == null) {
            return null; //Stop batch job
        }
        return new LayerCompressionDataItem(readLayerImages(area.getBlock()), area);
    }

    private List<List<BufferedImage>> readLayerImages(List<List<String>> block) throws IOException {
        List<List<BufferedImage>> files = new ArrayList<>();
        for(List<String> rowBlock : block) {
            List<BufferedImage> rowImages = new ArrayList<>();
            for(String key : rowBlock) {
                if(DUMMY.equals(key)) {
                    rowImages.add(getSingletonDummyImage());
                } else {
                    rowImages.add(ImageIO.read(new File(basePath + "/" + ORIGINAL_RESOLUTION + POPULATION_IMG_FOLDER, key + ".png")));
                }
            }
            files.add(rowImages);
        }
        return files;
    }

    public BufferedImage getSingletonDummyImage() {
        return MAP.computeIfAbsent(DUMMY, k -> ImageTransformations.getTransparentDummyImage(1200, 1200, BufferedImage.TYPE_4BYTE_ABGR));
    }
}
