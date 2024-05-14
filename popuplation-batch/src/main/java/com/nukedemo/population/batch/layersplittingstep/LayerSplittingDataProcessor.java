package com.nukedemo.population.batch.layersplittingstep;

import com.nukedemo.GhslMetaData;
import com.nukedemo.ImageTransformations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nukedemo.population.batch.layersplittingstep.LayerSplittingStepConfiguration.ORIGINAL_RESOLUTION;
import static com.nukedemo.population.batch.layersplittingstep.LayerSplittingStepConfiguration.SPLIT_FACTOR;
import static com.nukedemo.population.batch.shared.BatchUtils.*;

@Slf4j
@Service
@StepScope
public class LayerSplittingDataProcessor implements ItemProcessor<LayerSplittingDataItem, LayerSplittingDataItem> {

    @Autowired
    LayerSplittingStepCompletionListener layerSplittingStepCompletionListener;

    public LayerSplittingDataProcessor() {
    }

    @Override
    public LayerSplittingDataItem process(LayerSplittingDataItem item) throws IOException {
        item.setSplitImages(splitImages(item.getInputItem(), item.getSource()));
        return item;
    }

    private Map<String, byte[]> splitImages(LayerSplittingInputItem inputItem, BufferedImage source) throws IOException {
        String[] rc = getRowColumnFromKey(ORIGINAL_RESOLUTION, inputItem.getKey());
        List<List<BufferedImage>> split = ImageTransformations.splitImage(source, 4);
        Map<String, byte[]> res = new HashMap<>();
        Map<String, GhslMetaData> sample = new HashMap<>();
        for (int row = 0; row < split.size(); row++) {
            for (int col = 0; col < split.size(); col++) {
                String name = createName(rc, row + 1, col + 1);
                BufferedImage image = split.get(row).get(col);
                byte[] data = bufferedImageToByteArray(image, "PNG");
                res.put(name, data);
                GhslMetaData metaData = createMetadata(image, inputItem.getMetaData(), row, col);
                layerSplittingStepCompletionListener.addMetaData(name, metaData);
                sample.put(name, metaData);

            }
        }
        return res;
    }

    private GhslMetaData createMetadata(BufferedImage image, GhslMetaData origin, int row, int col) {
        double heightStep = (origin.getAreaHeight() * origin.getPixelHeightDegrees()) / SPLIT_FACTOR;
        double widthStep = (origin.getAreaWidth() * origin.getPixelWidthDegrees()) / SPLIT_FACTOR;
        double top = origin.getTopLeftCorner()[0] - (heightStep * col);
        double left = origin.getTopLeftCorner()[1] - (widthStep * row);
        double bottom = origin.getTopLeftCorner()[0] - (heightStep * (col + 1));
        double right = origin.getTopLeftCorner()[1] - (widthStep * (row + 1));
        //Measurement units degrees
        return GhslMetaData.builder()
                .withAreaWidth(image.getWidth())
                .withAreaHeight(image.getHeight())
                .withTopRightCorner(new double[]{top, right})
                .withBottomLeftCorner(new double[]{bottom, left})
                .withTopLeftCorner(new double[]{top, left})
                .withBottomRightCorner(new double[]{bottom, right})
                .withPixelHeightDegrees(heightStep)
                .withPixelWidthDegrees(widthStep)
                .build();
    }

    private String createName(String[] rc, int row, int col) {
        String rowName = rc[0] + "-" + row;
        String colName = rc[1] + "-" + col;
        return DATA_PREFIX + ORIGINAL_RESOLUTION + DATA_VERSION + rowName + DATA_COLUMN + colName;
    }

    private String[] getRowColumnFromKey(String resolution, String key) {
        Pattern regex = Pattern.compile( DATA_PREFIX + resolution + DATA_VERSION + "([0-9]+)" + DATA_COLUMN + "([0-9]+)");
        Matcher matcher = regex.matcher(key);
        if(matcher.find()) {
            return new String[] {matcher.group(1), matcher.group(2)};
        } else {
            throw new IllegalArgumentException("No key data found");
        }
    }

}
