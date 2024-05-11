package com.nukedemo.population.batch.layersplittingstep;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@Service
@StepScope
public class LayerSplittingDataReader implements ItemReader<LayerSplittingDataItem> {

    private LinkedList<LayerSplittingInputItem> areas;

    public LayerSplittingDataReader(@Value("#{stepExecutionContext['area']}") List<LayerSplittingInputItem> areas) {
        this.areas = new LinkedList<>(areas);
    }

    @Override
    public LayerSplittingDataItem read() throws IOException {
        LayerSplittingInputItem area = areas.poll();
        if (area == null) {
            return null; //Stop batch job
        }
        return new LayerSplittingDataItem(area, ImageIO.read(area.getFile()));
    }

}
