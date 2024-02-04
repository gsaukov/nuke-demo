package com.nukedemo.population.batch.ghslstep;

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
public class GhslFileDataReader implements ItemReader<GhslFileDataItem> {

    @Value("${populationBatch.ghsl.resolution}")
    private String resolution;

    @Autowired
    GhslApiClient ghslApiClient;
    private LinkedList<GhslFileInputItem> areas;

    public GhslFileDataReader(@Value("#{stepExecutionContext['area']}") List<GhslFileInputItem> areas) {
        this.areas = new LinkedList<>(areas);
    }

    @Override
    public GhslFileDataItem read() {
        GhslFileInputItem area = areas.poll();
        if(area == null){
            return null; //Stop batch job
        }
        byte[] ghslData = ghslApiClientFileDownload(area.getRow(), area.getColumn());
        log.info("Downloaded: R" + area.getRow() + "_C" + area.getColumn());
        return new GhslFileDataItem(area, ghslData);
    }

    public byte[] ghslApiClientFileDownload(int row, int column) {
        try {
            return ghslApiClient.downloadZipFile(resolution, row, column);
        } catch (Exception e) {
            log.error("Error downloading: R" + row + "_C" + column);
            return new byte [0];
        }
    }

}
