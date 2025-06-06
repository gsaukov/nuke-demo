# nuke-demo.
🥳
## Spring batch async cloud computing and master slave setup

Usually done with JMS/Kafka layer integrating master workers. 

* Remote Chunking - only processor/writer actions are scaled/delegated to worker nodes. The advantage of this approach is that you can feed chuck of work from the live systems.
* Remote Partitioning - all steps (read, process, write) delegated to worker nodes.

https://stackoverflow.com/questions/20323229/difference-between-spring-batch-remote-chunking-and-remote-partitioning  
https://medium.com/@eddybayonne1/remote-chunking-with-spring-batch-integration-63c9df75e361  
https://arnoldgalovics.com/spring-batch-remote-partitioning-kafka/  



## External sources:
* Openstreet map tag info: https://wiki.openstreetmap.org/wiki/Tag%3Amilitary%3Dbase
* Openstreet map key info: https://wiki.openstreetmap.org/wiki/Key:military
* Available tags and usage: https://taginfo.openstreetmap.org/keys/military#overview 
* Overview: https://securityforcemonitor.org/2018/07/06/openstreetmap-is-sometimes-a-handy-database-of-national-security-locations-heres-how-to-see-them/
* [GeoJSON validation](https://geojsonlint.com/)
* [GeoJSON visualization](https://geojson.io/)
* [Population density matrixes data](https://ghsl.jrc.ec.europa.eu/download.php)
* [GeoTiff JS Lib](https://geotiffjs.github.io/)


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

Search city boundaries:

    {{geocodeArea: Munich }};
    rel(pivot);
    // print results
    out body geom;

## Libraries
mapbox-sdk-turf might be handy, has turf functions and other GeoJSON specific things [Mapbox turf SDK](https://docs.mapbox.com/android/java/guides/turf/)  

    implementation 'com.mapbox.mapboxsdk:mapbox-sdk-turf:5.8.0'

## Pop maps:
Data: https://jeodpp.jrc.ec.europa.eu/ftp/jrc-opendata/GHSL/GHS_POP_GLOBE_R2023A/GHS_POP_E2025_GLOBE_R2023A_54009_100/V1-0/  
Parsing: https://taylor.callsen.me/parsing-geotiff-files-in-java/  

## Queries

[Check available queries!](./queries.md)

# UI Client

[Client](https://nuke-calculator.netlify.app/)

[GitHub Repo](https://github.com/gsaukov/nuke-client)

## OpenStreetMap graphana
https://prometheus.openstreetmap.org/d/bfz1HzFMk/network?orgId=1&from=now-24h&to=now&timezone=utc&var-instance=albi&var-external=enp1s0f0&var-internal=&refresh=1m
