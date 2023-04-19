package com.nukedemo.core.batch;

import com.nukedemo.core.batch.inputmodel.InputItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@StepScope
public class GeoDataReader implements ItemReader<String> {

    private UUID uuid = UUID.randomUUID();
    private LinkedList<InputItem> countries;

    public GeoDataReader(@Value("#{stepExecutionContext['country']}") List<InputItem> countries) {
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
