package com.nukedemo.population.batch;

import lombok.Data;

import java.io.Serializable;

@Data
public class PopulationInputItem implements Serializable {

	private final int row;

	private final int column;

}