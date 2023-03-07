package com.nukedemo.core.services.overpass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OverpassFeaturesItem {

	@JsonProperty("geometry")
	OverpassGeometry geometry;

	@JsonProperty("id")
	String id;

	@JsonProperty("type")
	String type;

	@JsonProperty("properties")
	OverpassProperties properties;

}
