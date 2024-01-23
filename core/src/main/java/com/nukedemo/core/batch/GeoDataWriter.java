package com.nukedemo.core.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@StepScope
public class GeoDataWriter implements ItemWriter<GeoDataItem> {

    private static final String OSM_FOLDER = "./data/res/osm/";
    private static final String GEOJSON_FOLDER = "./data/res/geojson/";
    private static final String OSM_EXTENSION = "_osm.json";
    private static final String GEOJSON_EXTENSION = ".geojson";

    private UUID uuid = UUID.randomUUID();

    public GeoDataWriter() throws IOException {
        Files.createDirectories(Paths.get(OSM_FOLDER));
        Files.createDirectories(Paths.get(GEOJSON_FOLDER));
    }

    @Override
    public void write(Chunk<? extends GeoDataItem> chunk) throws Exception {
        //write chunks of objects to documents
        for(GeoDataItem item : chunk.getItems()){
//            log.info("Writer ID:" + uuid + " Item: " + item.getCountryGeoJson());
            writeToFile(OSM_FOLDER + item.getCountryName() + OSM_EXTENSION, item.getCountryOsm());
            writeToFile(GEOJSON_FOLDER + item.getCountryName() + GEOJSON_EXTENSION, item.getCountryGeoJson());
        }
    }

    private void writeToFile(String fileName, String data) {
        try (FileWriter writer = new FileWriter(fileName);) {
            writer.write(data);
        }
        catch (Exception e) {
            log.error("Failed to create file for: " + fileName, e);
        }
    }

}
