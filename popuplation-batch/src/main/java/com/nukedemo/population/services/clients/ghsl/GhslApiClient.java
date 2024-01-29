package com.nukedemo.population.services.clients.ghsl;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

//https://jeodpp.jrc.ec.europa.eu/ftp/jrc-opendata/GHSL/GHS_POP_GLOBE_R2023A/GHS_POP_E2025_GLOBE_R2023A_54009_100/V1-0/tiles/GHS_POP_E2025_GLOBE_R2023A_54009_100_V1_0_R1_C13.zip
@FeignClient(value = "ghslApiClient",
        url = "https://jeodpp.jrc.ec.europa.eu/")
public interface GhslApiClient {

    String URL_PATH = "ftp/jrc-opendata/GHSL/GHS_POP_GLOBE_R2023A/GHS_POP_E2025_GLOBE_R2023A_54009_100/V1-0/tiles/GHS_POP_E2025_GLOBE_R2023A_54009_100_V1_0_R{row}_C{column}.zip";

    @RequestMapping(method = GET, value = URL_PATH)
    byte[] downloadZipFile(@PathVariable("row") int row, @PathVariable("column") int column);


    @RequestMapping(method = HEAD, value = URL_PATH)
    Response checkFileExists(@PathVariable("row") int row, @PathVariable("column") int column);

}

