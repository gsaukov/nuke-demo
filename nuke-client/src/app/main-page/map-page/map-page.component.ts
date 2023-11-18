import {Component, OnInit} from '@angular/core';
import Map from 'ol/Map';
import {MapService} from "../../services/map.service";
import {Feature as TurfFeature, MultiPolygon, Polygon} from "@turf/turf";
import {NominatimService} from "../../services/nominatim.service";
import {OverpassService} from "../../services/overpass.service";
import {Observable} from "rxjs";
import * as osm2geojson from 'osm2geojson-lite';


@Component({
  selector: 'app-map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.css']
})
export class MapPageComponent implements OnInit {

  private COUNTRY_ID_PREFIX = "36";

  private map!: Map;

  constructor(private mapService: MapService, private nominatimService: NominatimService, private overpassService: OverpassService) {
  }

  ngOnInit(): void {
    this.map = this.mapService.buildMap(this.map);
    this.nominatimService.getCityData('Moscow')
      .subscribe(nominatimRes => this.getOverpassData(nominatimRes)
        .subscribe(overpassRes => this.printOnMap(overpassRes))
      );

    // this.mapService.addGeometryLayer(this.map, this.mapService.geoJsonObject)
    // this.mapService.addCircles(this.map, this.mapService.geoJsonObject.features[0], 10, 2000)
  }


  private getOverpassData (data:any):Observable<any> {
    console.log(data)
    const countryId = this.getOverpassCountryId(data[0].osm_id)
    return this.overpassService.getGeometryData(countryId);
  }

  private getOverpassCountryId(osmId:number) {
    const formatted = String(osmId).padStart(8, '0');
    return this.COUNTRY_ID_PREFIX + formatted;
  }

  private printOnMap (overpassRes:any) {
    console.log(JSON.stringify(overpassRes))
    let geoJsonObject = osm2geojson(overpassRes, {completeFeature:true});
    console.log(JSON.stringify(geoJsonObject))
    this.mapService.addGeometryLayer(this.map, geoJsonObject)
    this.mapService.addCircles(this.map, geoJsonObject.features[0] as TurfFeature<(Polygon | MultiPolygon)>, 10, 2000)
  }

}
