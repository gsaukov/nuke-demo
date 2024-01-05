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

#### Query by id relation/way/node
https://www.overpass-api.de/api/interpreter?data=%5Bout%3Ajson%5D%3Barea(id%3A3602216724)%3Brel(pivot)%3Bout%20body%20geom%3B //uses 36 prefix  
https://www.overpass-api.de/api/interpreter?data=%5Bout%3Ajson%5D%3Barea(id%3A33612675)%3Bway(pivot)%3Bout%20geom%3B  
https://www.overpass-api.de/api/interpreter?data=%5Bout%3Ajson%5D%3Bnode(id%3A8674649717)%3Bout%20geom%3B

    area(3602216724);rel(pivot);out geom; //uses 36 prefix
    area(33612675);way(pivot);out geom;
    node(5921499312);out;
