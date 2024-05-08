package com.nukedemo.population.batch.layercompressionstep;

import com.nukedemo.GhslMetaData;
import com.nukedemo.ImageTransformations;
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

import static com.nukedemo.population.batch.layercompressionstep.LayerCompressionDataPartitioner.getGhslKey;
import static com.nukedemo.population.batch.layercompressionstep.LayerCompressionStepConfiguration.TARGET_RESOLUTION;

@Slf4j
@Service
@StepScope
public class LayerCompressionDataProcessor implements ItemProcessor<LayerCompressionDataItem, LayerCompressionDataItem> {

    @Autowired
    LayerCompressionStepCompletionListener layerCompressionStepCompletionListener;

    public LayerCompressionDataProcessor() {
    }

    @Override
    public LayerCompressionDataItem process(LayerCompressionDataItem item) throws IOException {
        BufferedImage merged = mergeImages(item.getBlock());
        merged = ImageTransformations.compressImage(merged, 3);
        item.setCompressedLayer(toByteArray(merged));
        updateGlobalMetadata(item, merged.getWidth(), merged.getHeight());
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
            mergedImage = ImageTransformations.concatenateImagesVertically(mergedImage, horizontalMergedImages.get(i));
        }
        return mergedImage;
    }

    private byte[] toByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        return out.toByteArray();
    }

    private void updateGlobalMetadata(LayerCompressionDataItem item, int width, int height) {
        LayerCompressionInputItem  inputItem = item.getInputItem();
        GhslMetaData metaData = getMetaData(inputItem, width, height);
        layerCompressionStepCompletionListener.addMetaData(getGhslKey(TARGET_RESOLUTION, inputItem.getRow(), inputItem.getColumn()), metaData);
    }

    private GhslMetaData getMetaData(LayerCompressionInputItem inputItem, int width, int height) {
        double[] topLeftCorner = inputItem.getBlockDimensions()[0];
        double[] bottomRightCorner = inputItem.getBlockDimensions()[1];
        double[] topRightCorner = new double[]{bottomRightCorner[0], topLeftCorner[1]};
        double[] bottomLeftCorner = new double[]{topLeftCorner[0], bottomRightCorner[1]};
        //Measurement units degrees
        double pixelHeight = Math.abs(topRightCorner[1] - bottomRightCorner[1]) / height;
        double pixelWidth = Math.abs(bottomLeftCorner[0] - bottomRightCorner[0]) / width;
        return GhslMetaData.builder()
                .withAreaWidth(width)
                .withAreaHeight(height)
                .withTopRightCorner(topRightCorner)
                .withBottomLeftCorner(bottomLeftCorner)
                .withTopLeftCorner(topLeftCorner)
                .withBottomRightCorner(bottomRightCorner)
                .withPixelHeightDegrees(pixelHeight)
                .withPixelWidthDegrees(pixelWidth)
                .build();
    }

}
