package com.nukedemo.core.services.overpass.model;

import lombok.Data;

import java.util.List;

@Data
public class OverpassResponse {
	private List<OverpassFeaturesItem> features;
	private String copyright;
	private String generator;
	private String type;
	private String timestamp;

}