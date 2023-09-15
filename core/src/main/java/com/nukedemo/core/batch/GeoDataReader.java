package com.nukedemo.core.batch;

import com.nukedemo.core.batch.inputmodel.InputItem;
import com.nukedemo.core.services.clients.nominatim.NominatimApiClient;
import com.nukedemo.core.services.exceptions.NdException;
import com.nukedemo.core.services.utils.NdJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.nukedemo.core.services.clients.nominatim.NominatimApiClient.FEATURE_TYPE_COUNTRY;
import static com.nukedemo.core.services.clients.nominatim.NominatimApiClient.FORMAT;

@Slf4j
@Service
@StepScope
public class GeoDataReader implements ItemReader<String> {


    @Autowired
    NominatimApiClient nominatimClient;

    private UUID uuid = UUID.randomUUID();
    private LinkedList<InputItem> countries;

    public GeoDataReader(@Value("#{stepExecutionContext['country']}") List<InputItem> countries) {
        this.countries = new LinkedList<>(countries);
    }

    @Override
    public String read() throws NdException {
        InputItem country = countries.poll();
        if(country == null){
            return null; //Stop batch job
        }
        log.info("Reader ID: " + uuid + " Item: " + country.getName());
        //read items from nominatim
        FeatureCollection countryFeature = readCountryFromNominatim(country.getName());
        String osmId = getOsmId(countryFeature);
        log.info("Reader ID: " + uuid + " Item: " + country.getName() + ", OSM_ID: " + osmId);
        return country.getName();
    }

    private FeatureCollection readCountryFromNominatim(String countryName) throws NdException {
        String res = nominatimClient.search(FORMAT, FEATURE_TYPE_COUNTRY, countryName);
        return NdJsonUtils.fromJson(res, FeatureCollection.class);
    }

    private String getOsmId(FeatureCollection countryFeature) {
        return countryFeature.getFeatures().get(0).<Map>getProperty("geocoding").get("osm_id").toString();
    }
}
