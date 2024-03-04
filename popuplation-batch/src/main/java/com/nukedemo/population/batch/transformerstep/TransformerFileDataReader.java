package com.nukedemo.population.batch.transformerstep;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@StepScope
public class TransformerFileDataReader implements ItemReader<TransformerDataItem> {

    private LinkedList<TransformerInputItem> areas;

    public TransformerFileDataReader(@Value("#{stepExecutionContext['area']}") List<TransformerInputItem> areas) {
        this.areas = new LinkedList<>(areas);
    }

    @Override
    public TransformerDataItem read() {
        return null;
    }

}
