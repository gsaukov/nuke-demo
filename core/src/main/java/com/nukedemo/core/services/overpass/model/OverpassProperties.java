package com.nukedemo.core.services.overpass.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OverpassProperties {

	@JsonProperty("@relations")
	List<OverpassRelationsItem> relations;

	@JsonProperty("@id")
	String id;

	@JsonProperty("name")
	String name;

	@JsonProperty("military")
	String military;

	@JsonProperty("landuse")
	String landuse;

	@JsonProperty("addr:housenumber")
	String addrHousenumber;

	@JsonProperty("ref")
	String ref;

	@JsonProperty("addr:street")
	String addrStreet;

	@JsonProperty("phone")
	String phone;

	@JsonProperty("phone_1")
	String phone1;

	@JsonProperty("addr:state")
	String addrState;

	@JsonProperty("description")
	String description;

	@JsonProperty("postal_code")
	String postalCode;

	@JsonProperty("addr:city")
	String addrCity;

	@JsonProperty("addr:postcode")
	String addrPostcode;

	@JsonProperty("end_date")
	String endDate;

	@JsonProperty("historic")
	String historic;

	@JsonProperty("start_date")
	String startDate;

	@JsonProperty("office")
	String office;

	@JsonProperty("addr:unit")
	String addrUnit;

	@JsonProperty("source")
	String source;

	@JsonProperty("website")
	String website;

	@JsonProperty("government")
	String government;

	@JsonProperty("wikipedia")
	String wikipedia;

	@JsonProperty("wikidata")
	String wikidata;

	@JsonProperty("aeroway")
	String aeroway;

	@JsonProperty("military_service")
	String militaryService;

	@JsonProperty("note")
	String note;

	@JsonProperty("check_date")
	String checkDate;

	@JsonProperty("addr:country")
	String addrCountry;

	@JsonProperty("image")
	String image;

	@JsonProperty("facebook")
	String facebook;

	@JsonProperty("operator")
	String operator;

	@JsonProperty("building")
	String building;

	@JsonProperty("email")
	String email;

	@JsonProperty("military_branch")
	String militaryBranch;

	@JsonProperty("operator:wikidata")
	String operatorWikidata;

	@JsonProperty("operator:wikipedia")
	String operatorWikipedia;

	@JsonProperty("alt_name")
	String altName;

	@JsonProperty("contact:facebook")
	String contactFacebook;

	@JsonProperty("source_ref")
	String sourceRef;

	@JsonProperty("seamark:type")
	String seamarkType;

	@JsonProperty("wheelchair")
	String wheelchair;

	@JsonProperty("air_conditioning")
	String airConditioning;

	@JsonProperty("opening_hours")
	String openingHours;

	@JsonProperty("abandoned")
	String abandoned;

	@JsonProperty("fixme")
	String fixme;

	@JsonProperty("contact:phone")
	String contactPhone;

	@JsonProperty("addr:county")
	String addrCounty;

	@JsonProperty("level")
	String level;

	@JsonProperty("nysgissam:nysaddresspointid")
	String nysgissamNysaddresspointid;

	@JsonProperty("nysgissam:review")
	String nysgissamReview;

	@JsonProperty("surface")
	String surface;

	@JsonProperty("lit")
	String lit;

	@JsonProperty("faa")
	String faa;

	@JsonProperty("access")
	String access;

	@JsonProperty("entrance")
	String entrance;

	@JsonProperty("seamark:rescue_station:category")
	String seamarkRescueStationCategory;

	@JsonProperty("iata")
	String iata;

	@JsonProperty("icao")
	String icao;

	@JsonProperty("man_made")
	String manMade;

	@JsonProperty("removed:amenity")
	String removedAmenity;

	@JsonProperty("razed")
	String razed;

	@JsonProperty("ref:miabldg")
	String refMiabldg;

	@JsonProperty("height")
	String height;

	@JsonProperty("type")
	String type;

	@JsonProperty("amenity")
	String amenity;

	@JsonProperty("tourism")
	String tourism;

	@JsonProperty("information")
	String information;

	@JsonProperty("short_name")
	String shortName;

	@JsonProperty("contact:email")
	String contactEmail;

	@JsonProperty("name:de")
	String nameDe;

	@JsonProperty("contact:website")
	String contactWebsite;

	@JsonProperty("contact:fax")
	String contactFax;

	@JsonProperty("short_name:de")
	String shortNameDe;

	@JsonProperty("sport")
	String sport;

	@JsonProperty("abandoned:aeroway")
	String abandonedAeroway;

	@JsonProperty("gnis:county_name")
	String gnisCountyName;

	@JsonProperty("gnis:feature_type")
	String gnisFeatureType;

	@JsonProperty("gnis:created")
	String gnisCreated;

	@JsonProperty("gnis:feature_id")
	String gnisFeatureId;

	@JsonProperty("ele")
	String ele;

	@JsonProperty("telephone")
	String telephone;

	@JsonProperty("ford")
	String ford;

	@JsonProperty("name_2")
	String name2;

	@JsonProperty("name_1")
	String name1;

	@JsonProperty("gnis:Class")
	String gnisClass;

	@JsonProperty("brand:wikidata")
	String brandWikidata;

	@JsonProperty("brand")
	String brand;

	@JsonProperty("name:en")
	String nameEn;

	@JsonProperty("fax")
	String fax;

	@JsonProperty("addr:door")
	String addrDoor;

	@JsonProperty("addr:place")
	String addrPlace;

	@JsonProperty("room")
	String room;

	@JsonProperty("addr:suite")
	String addrSuite;

	@JsonProperty("addr:street:type")
	String addrStreetType;

	@JsonProperty("addr:street:name")
	String addrStreetName;

	@JsonProperty("operator:type")
	String operatorType;

	@JsonProperty("addr:full")
	String addrFull;

	@JsonProperty("usar_addr:edit_date")
	String usarAddrEditDate;

	@JsonProperty("name:etymology:wikidata")
	String nameEtymologyWikidata;

	@JsonProperty("name:etymology")
	String nameEtymology;

	@JsonProperty("FAA:LFSN")
	String fAALFSN;

	@JsonProperty("FAA_LID")
	String fAALID;

	@JsonProperty("closest_town")
	String closestTown;

	@JsonProperty("FAA")
	String fAA;

	@JsonProperty("is_in")
	String isIn;

	@JsonProperty("faa_lid")
	String faaLid;

	@JsonProperty("is_in:iso_3166_2")
	String isInIso31662;

	@JsonProperty("internet_access")
	String internetAccess;

	@JsonProperty("name:nl")
	String nameNl;

	@JsonProperty("name:fi")
	String nameFi;

	@JsonProperty("int_name")
	String intName;

	@JsonProperty("name:kr")
	String nameKr;

	@JsonProperty("nat_name")
	String natName;

	@JsonProperty("name:fr")
	String nameFr;

	@JsonProperty("loc_name")
	String locName;

	@JsonProperty("aerodrome:type")
	String aerodromeType;

	@JsonProperty("old_name")
	String oldName;

	@JsonProperty("owner")
	String owner;

	@JsonProperty("name:fa")
	String nameFa;

	@JsonProperty("name:es")
	String nameEs;

	@JsonProperty("gnis:import_uuid")
	String gnisImportUuid;

	@JsonProperty("gnis:reviewed")
	String gnisReviewed;

	@JsonProperty("created_by")
	String createdBy;

	@JsonProperty("source:population")
	String sourcePopulation;

	@JsonProperty("source:population:date")
	String sourcePopulationDate;

	@JsonProperty("is_in:state_code")
	String isInStateCode;

	@JsonProperty("place")
	String place;

	@JsonProperty("population")
	String population;

	@JsonProperty("restriction")
	String restriction;

	@JsonProperty("highway")
	String highway;

	@JsonProperty("foot")
	String foot;

	@JsonProperty("tiger:cfcc")
	String tigerCfcc;

	@JsonProperty("tiger:name_base")
	String tigerNameBase;

	@JsonProperty("tiger:name_type")
	String tigerNameType;

	@JsonProperty("oneway")
	String oneway;

	@JsonProperty("tiger:county")
	String tigerCounty;

	@JsonProperty("tiger:reviewed")
	String tigerReviewed;

	@JsonProperty("tiger:name_base_1")
	String tigerNameBase1;

	@JsonProperty("tiger:name_type_1")
	String tigerNameType1;

	@JsonProperty("seamark:restricted_area:restriction")
	String seamarkRestrictedAreaRestriction;

	@JsonProperty("seamark:restricted_area:category")
	String seamarkRestrictedAreaCategory;

	@JsonProperty("barrier")
	String barrier;

	@JsonProperty("building:levels")
	String buildingLevels;

	@JsonProperty("indoor")
	String indoor;

	@JsonProperty("nrhp:criteria")
	String nrhpCriteria;

	@JsonProperty("nrhp:nhl")
	String nrhpNhl;

	@JsonProperty("protection_title")
	String protectionTitle;

	@JsonProperty("mappingdc:gid")
	String mappingdcGid;

	@JsonProperty("heritage:website")
	String heritageWebsite;

	@JsonProperty("heritage")
	String heritage;

	@JsonProperty("heritage:operator")
	String heritageOperator;

	@JsonProperty("dcgis:featurecode")
	String dcgisFeaturecode;

	@JsonProperty("nrhp:inscription_date")
	String nrhpInscriptionDate;

	@JsonProperty("dcgis:dataset")
	String dcgisDataset;

	@JsonProperty("ref:nrhp")
	String refNrhp;

	@JsonProperty("dcgis:captureyear")
	String dcgisCaptureyear;

	@JsonProperty("building:part")
	String buildingPart;

	@JsonProperty("area")
	String area;

	@JsonProperty("operator:short")
	String operatorShort;

	@JsonProperty("fence_type")
	String fenceType;

	@JsonProperty("admin_level")
	String adminLevel;

	@JsonProperty("layer")
	String layer;

	@JsonProperty("old_name:1872-1948")
	String oldName18721948;

	@JsonProperty("official_name")
	String officialName;

	@JsonProperty("wikimedia_commons")
	String wikimediaCommons;

	@JsonProperty("base_function")
	String baseFunction;

	@JsonProperty("mapillary")
	String mapillary;

	@JsonProperty("roof:shape")
	String roofShape;

	@JsonProperty("historic:civilization")
	String historicCivilization;

	@JsonProperty("ruins")
	String ruins;

	@JsonProperty("name:ko")
	String nameKo;

	@JsonProperty("name:zh-Hans")
	String nameZhHans;

	@JsonProperty("name:zh-Hant")
	String nameZhHant;

	@JsonProperty("name:zh")
	String nameZh;

	@JsonProperty("building:material")
	String buildingMaterial;

	@JsonProperty("start")
	String start;

	@JsonProperty("old_name:1920-1939")
	String oldName19201939;

	@JsonProperty("source_date")
	String sourceDate;

	@JsonProperty("roof:orientation")
	String roofOrientation;

	@JsonProperty("roof:colour")
	String roofColour;

	@JsonProperty("roof:material")
	String roofMaterial;

	@JsonProperty("roof:height")
	String roofHeight;

	@JsonProperty("building:colour")
	String buildingColour;

	@JsonProperty("gnis:fcode")
	String gnisFcode;

	@JsonProperty("gnis:id")
	String gnisId;

	@JsonProperty("gnis:county_id")
	String gnisCountyId;

	@JsonProperty("gnis:state_id")
	String gnisStateId;

	@JsonProperty("not:operator:wikidata")
	String notOperatorWikidata;

	@JsonProperty("emergency")
	String emergency;

	@JsonProperty("intermittent")
	String intermittent;

	@JsonProperty("old_name:1934-139")
	String oldName1934139;

	@JsonProperty("occupant:day")
	String occupantDay;

	@JsonProperty("material")
	String material;

	@JsonProperty("building:use")
	String buildingUse;

	@JsonProperty("seamark:status")
	String seamarkStatus;

	@JsonProperty("smoking")
	String smoking;

	@JsonProperty("outdoor_seating")
	String outdoorSeating;

	@JsonProperty("training")
	String training;

	@JsonProperty("name:signed")
	String nameSigned;

	@JsonProperty("roof:levels")
	String roofLevels;

	@JsonProperty("AWATER")
	String aWATER;

	@JsonProperty("Tiger:MTFCC")
	String tigerMTFCC;

	@JsonProperty("tiger:STATEFP")
	String tigerSTATEFP;

	@JsonProperty("tiger:COUNTYFP")
	String tigerCOUNTYFP;

	@JsonProperty("tiger:AREAID")
	String tigerAREAID;

	@JsonProperty("ALAND")
	String aLAND;

	@JsonProperty("variation")
	String variation;

	@JsonProperty("alt_name_1")
	String altName1;

	@JsonProperty("alt_name_2")
	String altName2;

	@JsonProperty("military_1")
	String military1;

	@JsonProperty("full_name")
	String fullName;

	@JsonProperty("name:ur")
	String nameUr;

	@JsonProperty("not:brand:wikidata")
	String notBrandWikidata;

	@JsonProperty("colour")
	String colour;

	@JsonProperty("name:pl")
	String namePl;

	@JsonProperty("boundary")
	String boundary;

	@JsonProperty("ref:miaaddr")
	String refMiaaddr;

	@JsonProperty("construction")
	String construction;

	@JsonProperty("lacounty:ain")
	String lacountyAin;

	@JsonProperty("lacounty:bld_id")
	String lacountyBldId;

	@JsonProperty("boat:repair")
	String boatRepair;

	@JsonProperty("marine")
	String marine;

	@JsonProperty("service")
	String service;

	@JsonProperty("maritime")
	String maritime;

	@JsonProperty("seamark:harbour:category")
	String seamarkHarbourCategory;

	@JsonProperty("disused:aeroway")
	String disusedAeroway;

	@JsonProperty("aerodrome")
	String aerodrome;

	@JsonProperty("addr:housename")
	String addrHousename;

	@JsonProperty("old_name:1916-1939")
	String oldName19161939;

	@JsonProperty("hazard")
	String hazard;

	@JsonProperty("gnis:ST_alpha")
	String gnisSTAlpha;

	@JsonProperty("gnis:County_num")
	String gnisCountyNum;

	@JsonProperty("gnis:County")
	String gnisCounty;

	@JsonProperty("gnis:ST_num")
	String gnisSTNum;

	@JsonProperty("import_uuid")
	String importUuid;

	@JsonProperty("disused")
	String disused;

	@JsonProperty("AREAID")
	String aREAID;

	@JsonProperty("MTFCC")
	String mTFCC;

	@JsonProperty("comment")
	String comment;

	@JsonProperty("old_name:1875-1960")
	String oldName18751960;

	@JsonProperty("leisure")
	String leisure;

	@JsonProperty("ref:faa")
	String refFaa;

	@JsonProperty("nycdoitt:bin")
	String nycdoittBin;

	@JsonProperty("source:area")
	String sourceArea;

	@JsonProperty("short_name:es")
	String shortNameEs;

	@JsonProperty("description:es")
	String descriptionEs;

	@JsonProperty("description:en")
	String descriptionEn;

	@JsonProperty("ownership")
	String ownership;

	@JsonProperty("name:ru")
	String nameRu;

	@JsonProperty("old_name:1938-1950")
	String oldName19381950;

	@JsonProperty("attribution")
	String attribution;

	@JsonProperty("addr:street:prefix")
	String addrStreetPrefix;

	@JsonProperty("chicago:building_id")
	String chicagoBuildingId;

	@JsonProperty("addr:block_number")
	String addrBlockNumber;

	@JsonProperty("FMMP_modified")
	String fMMPModified;

	@JsonProperty("FMMP_reviewed")
	String fMMPReviewed;

	@JsonProperty("designation")
	String designation;

	@JsonProperty("architect")
	String architect;

	@JsonProperty("accuracy")
	String accuracy;

	@JsonProperty("acres")
	String acres;

	@JsonProperty("tiger:NAME")
	String tigerNAME;

	@JsonProperty("tiger:CLASSFP")
	String tigerCLASSFP;

	@JsonProperty("tiger:PCICBSA")
	String tigerPCICBSA;

	@JsonProperty("is_in:state")
	String isInState;

	@JsonProperty("is_in:country_code")
	String isInCountryCode;

	@JsonProperty("tiger:NAMELSAD")
	String tigerNAMELSAD;

	@JsonProperty("tiger:PLCIDFP")
	String tigerPLCIDFP;

	@JsonProperty("tiger:LSAD")
	String tigerLSAD;

	@JsonProperty("tiger:FUNCSTAT")
	String tigerFUNCSTAT;

	@JsonProperty("tiger:CPI")
	String tigerCPI;

	@JsonProperty("tiger:PLACEFP")
	String tigerPLACEFP;

	@JsonProperty("tiger:PLACENS")
	String tigerPLACENS;

	@JsonProperty("is_in:country")
	String isInCountry;

	@JsonProperty("tiger:PCINECTA")
	String tigerPCINECTA;

	@JsonProperty("dcgis:gis_id")
	String dcgisGisId;

	@JsonProperty("dataset")
	String dataset;

	@JsonProperty("police")
	String police;

	@JsonProperty("latitude")
	String latitude;

	@JsonProperty("longitude")
	String longitude;

	@JsonProperty("elevation")
	String elevation;

	@JsonProperty("sangis:BASENAME")
	String sangisBASENAME;

	@JsonProperty("name:ko-Latn")
	String nameKoLatn;

	@JsonProperty("name:ar")
	String nameAr;

	@JsonProperty("name:ja")
	String nameJa;

	@JsonProperty("name:ja-Latn")
	String nameJaLatn;

	@JsonProperty("name:zh_pinyin")
	String nameZhPinyin;

	@JsonProperty("massgis:ATT_DATE")
	String massgisATTDATE;

	@JsonProperty("massgis:OS_ID")
	String massgisOSID;

	@JsonProperty("massgis:ARTICLE97")
	String massgisARTICLE97;

	@JsonProperty("club")
	String club;

	@JsonProperty("massgis:ASSESS_ACR")
	String massgisASSESSACR;

	@JsonProperty("massgis:TOWN_ID")
	String massgisTOWNID;

	@JsonProperty("massgis:OS_DEED_BO")
	String massgisOSDEEDBO;

	@JsonProperty("massgis:FEE_OWNER")
	String massgisFEEOWNER;

	@JsonProperty("massgis:PUB_ACCESS")
	String massgisPUBACCESS;

	@JsonProperty("massgis:POLY_ID")
	String massgisPOLYID;

	@JsonProperty("massgis:FEESYM")
	String massgisFEESYM;

	@JsonProperty("massgis:EOEAINVOLV")
	String massgisEOEAINVOLV;

	@JsonProperty("massgis:SITE_NAME")
	String massgisSITENAME;

	@JsonProperty("massgis:DCAM_ID")
	String massgisDCAMID;

	@JsonProperty("massgis:LEV_PROT")
	String massgisLEVPROT;

	@JsonProperty("massgis:FY_FUNDING")
	String massgisFYFUNDING;

	@JsonProperty("massgis:OWNER_TYPE")
	String massgisOWNERTYPE;

	@JsonProperty("massgis:PRIM_PURP")
	String massgisPRIMPURP;

	@JsonProperty("massgis:SOURCE_MAP")
	String massgisSOURCEMAP;

	@JsonProperty("massgis:OS_DEED_PA")
	String massgisOSDEEDPA;

	@JsonProperty("massgis:DEED_ACRES")
	String massgisDEEDACRES;

	@JsonProperty("massgis:OWNER_ABRV")
	String massgisOWNERABRV;

	@JsonProperty("massgis:BASE_MAP")
	String massgisBASEMAP;

	@JsonProperty("protected")
	String jsonMemberProtected;

	@JsonProperty("massgis:ASSESS_BLK")
	String massgisASSESSBLK;

	@JsonProperty("massgis:COMMENTS")
	String massgisCOMMENTS;

	@JsonProperty("massgis:ASSESS_MAP")
	String massgisASSESSMAP;

	@JsonProperty("massgis:ASSESS_LOT")
	String massgisASSESSLOT;

	@JsonProperty("massgis:CAL_DATE_R")
	String massgisCALDATER;

	@JsonProperty("year_of_construction")
	String yearOfConstruction;

	@JsonProperty("brand:wikipedia")
	String brandWikipedia;

	@JsonProperty("ref:fee")
	String refFee;

	@JsonProperty("old_name2")
	String oldName2;

	@JsonProperty("abbr_name")
	String abbrName;

	@JsonProperty("gnis:edited")
	String gnisEdited;

	@JsonProperty("url")
	String url;

	@JsonProperty("name:mk")
	String nameMk;

	@JsonProperty("old_name:1950--1992-06")
	String oldName1950199206;

	@JsonProperty("old_name:1941--1942-02-19")
	String oldName194119420219;

	@JsonProperty("old_name:1942-02-19--1950")
	String oldName194202191950;

	@JsonProperty("tiger:ALAND")
	String tigerALAND;

	@JsonProperty("tiger:AWATER")
	String tigerAWATER;

	@JsonProperty("tiger:INTPTLAT")
	String tigerINTPTLAT;

	@JsonProperty("tiger:FULLNAME")
	String tigerFULLNAME;

	@JsonProperty("tiger:INTPTLON")
	String tigerINTPTLON;

	@JsonProperty("dod:component")
	String dodComponent;

	@JsonProperty("service_branch")
	String serviceBranch;

	@JsonProperty("diagram:url")
	String diagramUrl;

	@JsonProperty("old_name:1")
	String oldName1;

	@JsonProperty("STATEFP")
	String sTATEFP;

	@JsonProperty("COUNTYFP")
	String cOUNTYFP;

	@JsonProperty("name:uk")
	String nameUk;

	@JsonProperty("name:it")
	String nameIt;

	@JsonProperty("name:hi")
	String nameHi;

	@JsonProperty("name:bn")
	String nameBn;

}