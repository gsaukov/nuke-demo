package com.nukedemo.core.services.clients.nominatim;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(value = "nominatimApiClient",
        url = "https://nominatim.openstreetmap.org/")
public interface NominatimApiClient {

    public static String FORMAT = "geocodejson";
    public static String FEATURE_TYPE_COUNTRY = "country";
    public static String FEATURE_TYPE_CITY = "city";

    @RequestMapping(method = GET, value = "search")
    String search(@RequestParam(name = "format") String format, @RequestParam(name = "featuretype") String featuretype, @RequestParam(name = "q") String q);

    //https://nominatim.openstreetmap.org/search?format=geocodejson&q=Greenland&featuretype=country
}