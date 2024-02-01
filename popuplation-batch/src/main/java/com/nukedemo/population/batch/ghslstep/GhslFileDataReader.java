package com.nukedemo.population.batch.ghslstep;

import com.nukedemo.population.batch.populationstep.PopulationDataItem;
import com.nukedemo.population.batch.populationstep.PopulationInputItem;
import com.nukedemo.population.services.clients.ghsl.GhslApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@StepScope
public class GhslFileDataReader implements ItemReader<PopulationDataItem> {

    @Autowired
    GhslApiClient ghslApiClient;
    private LinkedList<PopulationInputItem> areas;

    public GhslFileDataReader(@Value("#{stepExecutionContext['area']}") List<PopulationInputItem> areas) {
        this.areas = new LinkedList<>(areas);
    }

    @Override
    public PopulationDataItem read() {
        PopulationInputItem area = areas.poll();
        if(area == null){
            return null; //Stop batch job
        }
        String areaCode = "Row: " + area.getRow() + " Column: " + area.getColumn();
        log.info(areaCode);
        return new PopulationDataItem(areaCode);
    }

}
