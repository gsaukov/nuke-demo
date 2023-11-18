import {Injectable} from '@angular/core';
import Map from "ol/Map";
import View from "ol/View";
import {fromLonLat} from "ol/proj";
import TileLayer from "ol/layer/Tile";
import OSM from "ol/source/OSM";
import {GeoJSON} from "ol/format";
import VectorSource from "ol/source/Vector";
import {Vector} from "ol/layer";
import {Fill, Stroke, Style} from "ol/style";
import {Feature as TurfFeature} from "@turf/helpers/dist/js/lib/geojson";
import {MultiPolygon, Polygon} from "@turf/turf";
import {Feature} from "ol";
import {Circle} from "ol/geom";
import {TurfService} from "./turf.service";


@Injectable({
  providedIn: 'root'
})
export class MapService {

  constructor(private turfService: TurfService) {
  }

  private buildMap(map: Map) {
    map = new Map({
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
    map.addLayer(layer);
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
