package com.nukedemo.core.services.overpass.model;

import lombok.Data;

@Data
public class OverpassFeaturesItem {
	private OverpassGeometry geometry;
	private String id;
	private String type;
	private OverpassProperties properties;

}
