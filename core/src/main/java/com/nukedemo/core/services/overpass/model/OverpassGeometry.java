package com.nukedemo.core.services.overpass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OverpassGeometry {
	@JsonProperty("coordinates")
	List<List<List<Double>>> coordinates;

	@JsonProperty("type")
	String type;

}