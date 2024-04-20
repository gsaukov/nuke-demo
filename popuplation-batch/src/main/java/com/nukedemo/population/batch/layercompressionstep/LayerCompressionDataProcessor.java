package com.nukedemo.population.batch.layercompressionstep;

import com.nukedemo.ImageTransformations;
import com.nukedemo.population.batch.JobCompletionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@StepScope
public class LayerCompressionDataProcessor implements ItemProcessor<LayerCompressionDataItem, LayerCompressionDataItem> {

    @Autowired
    JobCompletionListener jobCompletionListener;

    public LayerCompressionDataProcessor() {
    }

    @Override
    public LayerCompressionDataItem process(LayerCompressionDataItem item) {

        return item;
    }

    private BufferedImage mergeImages(List<List<BufferedImage>> images) {
        List<BufferedImage> horizontalImages =new ArrayList<>();
        //concatenate horizontally.
        for(List<BufferedImage> row : images) {
            BufferedImage horizontalImage = row.get(0);
            for(int i = 1; i < row.size(); i++) {
                horizontalImage = ImageTransformations.concatenateImagesHorizontally(horizontalImage, row.get(i));
            }
            horizontalImages.add(horizontalImage);
        }

        //concatenate vertically.
        BufferedImage mergedImage = horizontalImages.get(0);
        for(int i = 1; i < horizontalImages.size(); i++) {
            mergedImage = ImageTransformations.concatenateImagesHorizontally(mergedImage, horizontalImages.get(i));
        }

        return mergedImage;
    }

}
