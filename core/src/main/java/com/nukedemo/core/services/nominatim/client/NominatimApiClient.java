package com.nukedemo.core.services.nominatim.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(value = "nominatimApiClient",
        url = "https://nominatim.openstreetmap.org/")
public interface NominatimApiClient {

    @RequestMapping(method = GET, value = "search")
    String search(@RequestParam(name = "format") String format, @RequestParam(name = "q") String q);

    //https://nominatim.openstreetmap.org/search?format=json&q=Canada
}