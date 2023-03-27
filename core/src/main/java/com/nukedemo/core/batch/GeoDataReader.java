package com.nukedemo.core.batch;

import com.nukedemo.core.batch.inputmodel.InputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class GeoDataReader implements ItemReader<String> {

    private UUID uuid = UUID.randomUUID();
    private LinkedList<InputItem> countries;

    public GeoDataReader(List<InputItem> countries) {
        this.countries = new LinkedList<>(countries);
    }

    @Override
    public String read() {
        InputItem country = countries.poll();
        if(country == null){
            return null; //Stop batch job
        }
        log.info("Reader ID: " + uuid + " Item: " + country.getName());
        //read items from nominatim
        return country.getName();
    }
}
