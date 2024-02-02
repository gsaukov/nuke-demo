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
        log.info("Downloading: row_" + area.getRow() + "_column_" + area.getColumn());
        byte[] ghslData = ghslApiClientFileDownload(area.getRow(), area.getColumn());
        return new GhslFileDataItem(area, ghslData);
    }

    public byte[] ghslApiClientFileDownload(int row, int column) {
        return ghslApiClient.downloadZipFile(row, column);
    }

}
