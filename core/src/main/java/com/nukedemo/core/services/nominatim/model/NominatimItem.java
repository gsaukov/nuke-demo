package com.nukedemo.core.services.nominatim.model;

import java.util.List;
import lombok.Data;

@Data
public class NominatimItem {
	private String osmType;
	private int osmId;
	private String licence;
	private List<String> boundingbox;
	private Object importance;
	private String icon;
	private String lon;
	private String displayName;
	private String type;
	private String jsonMemberClass;
	private int placeId;
	private String lat;
}

