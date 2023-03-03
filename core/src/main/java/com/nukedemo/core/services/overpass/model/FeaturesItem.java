package com.nukedemo.core.services.overpass.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeaturesItem{
	private Geometry geometry;
	private String id;
	private String type;
	private Properties properties;

}
