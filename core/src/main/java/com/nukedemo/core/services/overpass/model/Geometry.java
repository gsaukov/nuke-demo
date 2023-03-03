package com.nukedemo.core.services.overpass.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Geometry{
	private List<List<List<Object>>> coordinates;
	private String type;

}