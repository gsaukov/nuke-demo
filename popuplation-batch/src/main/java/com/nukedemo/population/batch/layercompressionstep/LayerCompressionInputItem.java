package com.nukedemo.population.batch.layercompressionstep;

import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.List;

@Data
public class LayerCompressionInputItem implements Serializable {

    private final List<List<String>> block;

}