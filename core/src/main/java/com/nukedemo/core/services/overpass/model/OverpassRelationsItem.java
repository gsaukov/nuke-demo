package com.nukedemo.core.services.overpass.model;

import lombok.Data;

@Data
public class OverpassRelationsItem {
	private String role;
	private OverpassReltags reltags;
	private String rel;

}
