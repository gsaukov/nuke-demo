import {Component, OnInit} from '@angular/core';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import {TurfService} from "../../services/turf.service";
import VectorSource from "ol/source/Vector";
import {Fill, Stroke, Style} from "ol/style";
import {Feature} from "ol";
import {Circle} from "ol/geom";
import {Vector} from "ol/layer";
import {fromLonLat} from "ol/proj";
import {GeoJSON} from "ol/format";
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

  constructor(private turfService: TurfService, private nominatimService: NominatimService, private overpassService: OverpassService) {
  }

  ngOnInit(): void {
    this.buildMap();
    this.nominatimService.getCityData('Moscow')
      .subscribe(nominatimRes => this.getOverpassData(nominatimRes)
        .subscribe(overpassRes => this.printOnMap(overpassRes))
      );

    // this.addGeometryLayer(this.map, this.turfService.geoJsonObject)
    // this.addCircles(this.map, this.turfService.geoJsonObject.features[0], 10, 2000)
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
    this.addGeometryLayer(this.map, geoJsonObject)
    this.addCircles(this.map, geoJsonObject.features[0] as TurfFeature<(Polygon | MultiPolygon)>, 10, 2000)
  }

  private buildMap() {
    this.map = new Map({
      view: new View({
        center: fromLonLat([69.2787079, 41.3123363], 'EPSG:3857'),
        zoom: 12,
      }),
      layers: [
        new TileLayer({
          source: new OSM(
            {
              attributions: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }),
        }),
      ],
      target: 'ol-map'
    });
  }

  private addGeometryLayer(map: Map, geoJsonObject: any) {
    const geojsonFormat = new GeoJSON();
    const vectorSource = new VectorSource();
    const features = geojsonFormat.readFeatures(geoJsonObject, {
      featureProjection: 'EPSG:3857',
    });
    vectorSource.addFeatures(features)
    const layer = new Vector({
      source: vectorSource,
      style: [
        new Style({
          stroke: new Stroke({color: 'blue', width: 3}),
          fill: new Fill({color: 'rgba(0, 0, 255, 0.1)'})
        })
      ]
    });
    this.map.addLayer(layer);
  }

  private addCircles(map: Map, polygon: TurfFeature<(Polygon | MultiPolygon)>, num: number, radius: number) {
    const vectorSource = new VectorSource();
    const layer = new Vector({source: vectorSource,});
    map.addLayer(layer);

    for (let i = 0; i < num; i++) {
      let coord = this.turfService.randomPointInPolygon(polygon).geometry.coordinates
      const circleFeature = new Feature(new Circle(fromLonLat(coord, 'EPSG:3857'), radius));
      circleFeature.setStyle(this.radialGraientStylre());
      vectorSource.addFeature(circleFeature);
    }
  }


  private radialGraientStylre(): Style {
    return new Style({
      renderer(coordinates: any, state: any) {
        const [[x, y], [x1, y1]] = coordinates;
        const ctx = state.context;
        const dx = x1 - x;
        const dy = y1 - y;
        const radius = Math.sqrt(dx * dx + dy * dy);

        const innerRadius = 0;
        const outerRadius = radius * 1.4;

        const gradient = ctx.createRadialGradient(x, y, innerRadius, x, y, outerRadius);
        gradient.addColorStop(0, 'rgba(255,0,0, 0.7)');
        gradient.addColorStop(0.5, 'rgba(255,115,0, 0.7)');
        gradient.addColorStop(0.6, 'rgba(0,157,255, 0.5)');
        gradient.addColorStop(0.7, 'rgba(255,255,255, 0.3)');
        gradient.addColorStop(1, 'rgba(255,255,255, 0.1)');
        ctx.beginPath();
        ctx.arc(x, y, radius, 0, 2 * Math.PI, true);
        ctx.fillStyle = gradient;
        ctx.fill();

        // ctx.arc(x, y, radius, 0, 2 * Math.PI, true);
        // ctx.strokeStyle = 'rgb(0,0,190)';
        // ctx.stroke();
      },
    })
  }
}
