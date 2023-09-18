package com.nukedemo.core.batch;

import com.nukedemo.core.batch.inputmodel.InputItem;
import com.nukedemo.core.services.clients.nominatim.NominatimApiClient;
import com.nukedemo.core.services.clients.overpass.OverpassApiClient;
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

    private static final String COUNTRY_ID_PREFIX = "36";
    private static final String OVERPASS_QUERY = "[out:json][timeout:180]; area(id:%s)->.searchArea; (way[\"military\"~\"barracks|office|danger_area|training_area|airfield|base|naval_base\"](area.searchArea); relation[\"military\"~\"barracks|office|danger_area|training_area|airfield|base|naval_base\"](area.searchArea);); out body;>;out skel qt;";


    @Autowired
    NominatimApiClient nominatimClient;

    @Autowired
    OverpassApiClient overpassClient;

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
        Integer osmId = getOsmId(countryFeature);
        String overpassCountryId = getOverpassCountryId(osmId);
        log.info("Reader ID: " + uuid + " Item: " + country.getName() + ", OSM_ID: " + osmId + "Nominatim ID" + overpassCountryId);
        queryOverpass(overpassCountryId);
        return country.getName();
    }

    private FeatureCollection readCountryFromNominatim(String countryName) throws NdException {
        String res = nominatimClient.search(FORMAT, FEATURE_TYPE_COUNTRY, countryName);
        return NdJsonUtils.fromJson(res, FeatureCollection.class);
    }

    private String queryOverpass(String overpassCountryId) {
        String overpassQuery = String.format(OVERPASS_QUERY, overpassCountryId);
        log.info(overpassQuery);
        String res = overpassClient.interpret(overpassQuery);
        log.info(res);
        return res;
    }

    private Integer getOsmId(FeatureCollection countryFeature) {
        return (Integer)countryFeature.getFeatures().get(0).<Map>getProperty("geocoding").get("osm_id");
    }

    private String getOverpassCountryId(Integer osmId) {
        String formatted = String.format("%08d", osmId);
        return COUNTRY_ID_PREFIX + formatted;
    }

}
