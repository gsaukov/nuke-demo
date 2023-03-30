# nuke-demo
🥳

## External sources:
* Openstreet map tag info: https://wiki.openstreetmap.org/wiki/Tag%3Amilitary%3Dbase
* Openstreet map key info: https://wiki.openstreetmap.org/wiki/Key:military
* Available tags and usage: https://taginfo.openstreetmap.org/keys/military#overview 
* Overview: https://securityforcemonitor.org/2018/07/06/openstreetmap-is-sometimes-a-handy-database-of-national-security-locations-heres-how-to-see-them/


## Samples
Query sample for [Overpass](https://overpass-turbo.eu/):

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