package com.nukedemo.core.services.overpass.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Response{
	private List<FeaturesItem> features;
	private String copyright;
	private String generator;
	private String type;
	private String timestamp;

}