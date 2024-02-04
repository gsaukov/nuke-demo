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

    @Autowired
    GhslApiClient ghslApiClient;

    private final String resolution = "4326_30ss";

    @Test
    public void testGhslApiClientFileDownload() throws IOException {
        byte[] res = ghslApiClient.downloadZipFile(resolution, 2, 24);
        File outputFile = new File("my.zip");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(res);
        }
        outputFile.delete();
    }

    @Test
    public void testGhslApiClientFileDoNotExistsCheck() {
        Response fileExists = ghslApiClient.checkFileExists(resolution, 6, 5);
        assertEquals(404, fileExists.status());
    }

    @Test
    public void testGhslApiClientFileExistsCheck() {
        Response fileExists = ghslApiClient.checkFileExists(resolution, 2, 24);
        assertEquals(200, fileExists.status());
    }

}