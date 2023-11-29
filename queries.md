# QUERY SAMPLES

#### TOOL
https://overpass-turbo.eu/

## TAGS:

#### WIKI
https://taginfo.openstreetmap.org/keys/military#values  
https://taginfo.openstreetmap.org/keys/highway#values

> aeroway=aerodrome
> highway=residential|service|unclassified|path|crossing|tertiary|secondary|primary|turning_circle|living_street|cycleway|trunk|motorway|motorway_link|trunk_link|primary_link|construction|secondary_link|tertiary_link|motorway_junction|turning_loop|mini_roundabout|road

## SAMPLES:

https://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_API_by_Example

#### Munich restaurants:

       [out:json];
       node(48.00,11.34,48.25,11.71)[amenity=restaurant];
       out center;


#### Munich Rewe with BB:

       [out:json];
       node(48.00,11.34,48.25,11.71)["name"~"REWE|rewe|Rewe"];
       out center;

#### Munich Rewe node/way/relation with area resolution:

       [out:json];
       area[name="München"]->.searchArea;
       (
       node["name"~"REWE|rewe|Rewe"](area.searchArea);
       way["name"~"REWE|rewe|Rewe"](area.searchArea);
       relation["name"~"REWE|rewe|Rewe"](area.searchArea);
       );
       out center;

#### Munich all shops with area resolution:

       [out:json];
       area[name="München"]->.searchArea;
       node["shop"](area.searchArea);
       out center;

#### Munich public roads:

       [out:json];
       area[name="München"]->.searchArea;
       (
       way["highway"~"residential|service|unclassified|path|crossing|tertiary|secondary|primary|turning_circle|living_street|cycleway|trunk|motorway|motorway_link|trunk_link|primary_link|construction|secondary_link|tertiary_link|motorway_junction|turning_loop|mini_roundabout|road"](area.searchArea);
       );
       out center;

#### Global BMW presence

       [out:json];
       (
       node["name"~"BMW"];
       way["name"~"BMW"];
       relation["name"~"BMW"];
       );
       out center;

#### Military objects in Canada

       // Limit the search to “Canada”
       {{geocodeArea:Canada}}->.searchArea;
       // Pull together the results that we want
       (
       // look for military objects using or
       way["military"~"barracks|office|danger_area|training_area|airfield|base|naval_base"](area.searchArea);
       relation["military"~"barracks|office|danger_area|training_area|airfield|base|naval_base"](area.searchArea);
       node["military"~"barracks|office|danger_area|training_area|airfield|base|naval_base"](area.searchArea);
       );
       // Print out the results
       out body;
       >;
       out skel qt;
