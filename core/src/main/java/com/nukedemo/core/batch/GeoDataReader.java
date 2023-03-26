package com.nukedemo.core.batch;

import com.nukedemo.core.batch.inputmodel.InputItem;
import org.springframework.batch.item.ItemReader;


public class GeoDataReader implements ItemReader<String> {

    private InputItem country;

    public GeoDataReader(InputItem country) {
        this.country = country;
    }

    @Override
    public String read() {
        return null;
    }
}
