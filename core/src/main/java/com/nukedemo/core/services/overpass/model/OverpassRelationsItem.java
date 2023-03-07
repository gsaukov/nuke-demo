package com.nukedemo.core.services.overpass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OverpassRelationsItem {
	@JsonProperty("role")
	String role;

	@JsonProperty("reltags")
	OverpassReltags reltags;

	@JsonProperty("rel")
	String rel;

}
