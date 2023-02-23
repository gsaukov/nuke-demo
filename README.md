# nuke-demo
🥳

* https://wiki.openstreetmap.org/wiki/Tag%3Amilitary%3Dbase
* https://wiki.openstreetmap.org/wiki/Key:military
* https://taginfo.openstreetmap.org/keys/military#overview
* https://securityforcemonitor.org/2018/07/06/openstreetmap-is-sometimes-a-handy-database-of-national-security-locations-heres-how-to-see-them/





https://overpass-turbo.eu/ Sample


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