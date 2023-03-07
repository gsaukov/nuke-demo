package com.nukedemo.core.services.overpass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OverpassReltags {

	@JsonProperty("website")
	String website;

	@JsonProperty("description")
	String description;

	@JsonProperty("military")
	String military;

	@JsonProperty("type")
	String type;

	@JsonProperty("building")
	String building;

	@JsonProperty("AWATER")
	String aWATER;

	@JsonProperty("AREAID")
	String aREAID;

	@JsonProperty("landuse")
	String landuse;

	@JsonProperty("name")
	String name;

	@JsonProperty("Tiger:MTFCC")
	String tigerMTFCC;

	@JsonProperty("ALAND")
	String aLAND;

	@JsonProperty("MTFCC")
	String mTFCC;

	@JsonProperty("source")
	String source;

	@JsonProperty("addr:housenumber")
	String addrHousenumber;

	@JsonProperty("addr:state")
	String addrState;

	@JsonProperty("addr:street")
	String addrStreet;

	@JsonProperty("addr:postcode")
	String addrPostcode;

	@JsonProperty("addr:unit")
	String addrUnit;

	@JsonProperty("addr:city")
	String addrCity;

	@JsonProperty("gnis:feature_id")
	String gnisFeatureId;

	@JsonProperty("military_service")
	String militaryService;

	@JsonProperty("name:etymology:wikidata")
	String nameEtymologyWikidata;

	@JsonProperty("name:etymology")
	String nameEtymology;

	@JsonProperty("seamark:restricted_area:restriction")
	String seamarkRestrictedAreaRestriction;

	@JsonProperty("seamark:status")
	String seamarkStatus;

	@JsonProperty("seamark:restricted_area:category")
	String seamarkRestrictedAreaCategory;

	@JsonProperty("seamark:type")
	String seamarkType;

	@JsonProperty("wikipedia")
	String wikipedia;

	@JsonProperty("wikidata")
	String wikidata;

}
