package com.nukedemo.core.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@StepScope
public class GeoDataWriter implements ItemWriter<GeoDataItem> {

    private UUID uuid = UUID.randomUUID();

    @Override
    public void write(Chunk<? extends GeoDataItem> chunk) throws Exception {
        //write chunks of objects to documents
        Files.createDirectories(Paths.get("./res"));
        for(GeoDataItem item : chunk.getItems()){
//            log.info("Writer ID:" + uuid + " Item: " + item.getCountryGeoJson());
            writeToFile(item.getCountryName(), item.getCountryOsm(), "_osm.json");
            writeToFile(item.getCountryName(), item.getCountryGeoJson(), ".geojson");
        }
    }

    private void writeToFile(String countryName, String data, String extension) {
        try (FileWriter writer = new FileWriter("./res/" + countryName + extension);) {
            writer.write(data);
        }
        catch (Exception e) {
            log.error("Failed to create file for: " + countryName, e);
        }
    }

}
