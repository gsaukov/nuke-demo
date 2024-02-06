package com.nukedemo.population.batch.populationstep;

import lombok.Data;

import java.io.File;
import java.io.Serializable;

@Data
public class PopulationInputItem implements Serializable {

	private final File file;

}