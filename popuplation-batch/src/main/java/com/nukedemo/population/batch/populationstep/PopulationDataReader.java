package com.nukedemo.population.batch.populationstep;

import com.nukedemo.population.services.clients.ghsl.GhslApiClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
@StepScope
public class PopulationDataReader implements ItemReader<PopulationDataItem> {

    @Autowired
    GhslApiClient ghslApiClient;
    private LinkedList<PopulationInputItem> areas;

    public PopulationDataReader(@Value("#{stepExecutionContext['area']}") List<PopulationInputItem> areas) {
        this.areas = new LinkedList<>(areas);
    }

    @Override
    public PopulationDataItem read() {
        PopulationInputItem area = areas.poll();

        if(area == null){
            return null; //Stop batch job
        }

        ZipEntryContainer zipEntryContainer = null;
        try {
            zipEntryContainer = extractTifFromZip(area.getFile());
        } catch (IOException e) {
            throw new RuntimeException("Zip Entry Extraction Failed." + area.getFile().getName(), e);
        }

        if(zipEntryContainer == null) {
            log.info("Tif file was not found in {} reading next entry.", area.getFile().getName());
            return read();
        } else {
            return new PopulationDataItem(removeExtension(zipEntryContainer.getEntryName()), zipEntryContainer.getSource());
        }
    }


    public ZipEntryContainer extractTifFromZip(File zipFile) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String entryName = null;
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = null;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                entryName = entry.getName();
                if (entryName.toLowerCase().endsWith(".tif")) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    return new ZipEntryContainer(entryName, outputStream.toByteArray());
                }
            }
        }
        return null;
    }

    private String removeExtension(String name) {
        return name.replace(".tif", "");
    }

    @Data
    private static class ZipEntryContainer {
        private final String entryName;
        private final byte[] source;
    }

}
