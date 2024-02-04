package com.nukedemo.population.batch.ghslstep;

import lombok.Data;

import java.io.Serializable;

@Data
public class GhslFileInputItem implements Serializable {

	private final String resolution;

	private final int row;

	private final int column;

}