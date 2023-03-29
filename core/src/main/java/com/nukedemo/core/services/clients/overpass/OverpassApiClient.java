package com.nukedemo.core.services.clients.overpass;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(value = "overpassApiClient",
        url = "https://overpass-api.de/")
public interface OverpassApiClient {

    @RequestMapping(method = POST, value = "api/interpreter")
    String interpret(@RequestBody String query);

}