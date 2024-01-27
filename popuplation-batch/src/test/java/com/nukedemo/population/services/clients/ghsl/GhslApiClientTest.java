package com.nukedemo.population.services.clients.ghsl;

import com.nukedemo.population.PopulationBatchApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Slf4j
@StepScope
@SpringBatchTest()
@EnableAutoConfiguration
@ContextConfiguration(classes = { PopulationBatchApplication.class })
class GhslApiClientTest {

    @Autowired
    GhslApiClient ghslApiClient;

    @Test
    public void testNominatimClient() throws IOException {
        byte [] res = ghslApiClient.downloadZipFile(2, 24);
        File outputFile = new File("my.zip");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(res);
        }
    }


}