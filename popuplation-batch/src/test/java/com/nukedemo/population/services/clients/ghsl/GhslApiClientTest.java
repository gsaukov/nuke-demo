package com.nukedemo.population.services.clients.ghsl;

import com.nukedemo.population.PopulationBatchApplication;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@StepScope
@SpringBatchTest()
@EnableAutoConfiguration
@ContextConfiguration(classes = {PopulationBatchApplication.class})
class GhslApiClientTest {

    private static final String WSG84_RESOLUTION = "4326_30ss";
    private static final String MOLLWIDE_RESOLUTION = "54009_1000";

    @Autowired
    GhslApiClient ghslApiClient;

    @Test
    public void testGhslApiClientFileDownload() throws IOException {
        byte[] res = ghslApiClient.downloadZipFile(WSG84_RESOLUTION, 2, 24);
        File outputFile = new File("my.zip");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(res);
        }
        outputFile.delete();
    }

    @Test
    public void testGhslApiClientWSG84FileDoNotExistsCheck() {
        Response fileExists = ghslApiClient.checkFileExists(WSG84_RESOLUTION, 6, 5);
        assertEquals(404, fileExists.status());
    }

    @Test
    public void testGhslApiClientWSG84FileExistsCheck() {
        Response fileExists = ghslApiClient.checkFileExists(WSG84_RESOLUTION, 2, 24);
        assertEquals(200, fileExists.status());
    }

    @Test
    public void testGhslApiClientMollwideFileDoNotExistsCheck() {
        Response fileExists = ghslApiClient.checkFileExists(MOLLWIDE_RESOLUTION, 6, 7);
        assertEquals(404, fileExists.status());
    }

    @Test
    public void testGhslApiClientMollwideFileExistsCheck() {
        Response fileExists = ghslApiClient.checkFileExists(MOLLWIDE_RESOLUTION, 2, 24);
        assertEquals(200, fileExists.status());
    }

}