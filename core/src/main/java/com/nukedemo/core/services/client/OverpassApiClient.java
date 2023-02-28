package com.nukedemo.core.services.client;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(value = "overpassApiClient",
        url = "${feign.client.config.overpassApiClient.url}")
public interface OverpassApiClient {

    @RequestMapping(method = POST, value = "api/interpreter", produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE)
    @Headers("Content-Type: application/json")
    String interpret(@RequestBody String query);

}