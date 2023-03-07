package com.nukedemo.core.services.overpass.model;

import lombok.Data;

import java.util.List;

@Data
public class OverpassGeometry {
	private List<List<List<Object>>> coordinates;
	private String type;

}