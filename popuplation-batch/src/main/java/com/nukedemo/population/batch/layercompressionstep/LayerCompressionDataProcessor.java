package com.nukedemo.population.batch.layercompressionstep;

import com.nukedemo.ImageTransformations;
import com.nukedemo.population.batch.JobCompletionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public LayerCompressionDataItem process(LayerCompressionDataItem item) throws IOException {
        BufferedImage merged = mergeImages(item.getBlock());
        merged = ImageTransformations.compressImage(merged, 3);
        item.setCompressedLayer(toByteArray(merged));
        return item;
    }

    private BufferedImage mergeImages(List<List<BufferedImage>> images) {
        List<BufferedImage> horizontalMergedImages =new ArrayList<>();
        //concatenate horizontally.
        for(List<BufferedImage> row : images) {
            BufferedImage horizontalImage = row.get(0);
            for(int i = 1; i < row.size(); i++) {
                horizontalImage = ImageTransformations.concatenateImagesHorizontally(horizontalImage, row.get(i));
            }
            horizontalMergedImages.add(horizontalImage);
        }
        //concatenate vertically.
        BufferedImage mergedImage = horizontalMergedImages.get(0);
        for(int i = 1; i < horizontalMergedImages.size(); i++) {
            mergedImage = ImageTransformations.concatenateImagesHorizontally(mergedImage, horizontalMergedImages.get(i));
        }
        return mergedImage;
    }

    private byte[] toByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        return out.toByteArray();
    }

}
