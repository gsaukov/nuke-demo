package com.nukedemo.population.batch.transformerstep;

import lombok.Data;

import java.io.File;
import java.io.Serializable;

@Data
public class TransformerInputItem implements Serializable {

    private final File file;

}