package com.nukedemo.core.services.overpass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OverpassResponse {
	@JsonProperty("features")
	List<OverpassFeaturesItem> features;

	@JsonProperty("copyright")
	String copyright;

	@JsonProperty("generator")
	String generator;

	@JsonProperty("type")
	String type;

	@JsonProperty("timestamp")
	String timestamp;

}