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
import {fromLonLat, transformExtent} from "ol/proj";
import {GeoJSON} from "ol/format";


@Component({
  selector: 'app-map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.css']
})
export class MapPageComponent implements OnInit {

  private map!: Map;

  constructor(private turfService: TurfService) {
  }

  ngOnInit(): void {
    this.map = new Map({
      view: new View({
        center: [0, 0],
        zoom: 1,
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

    const vectorSource = new VectorSource();

    const layer = new Vector({
      source: vectorSource,
      style: [
        new Style({
          stroke: new Stroke({
            color: 'blue',
            width: 3
          }),
          fill: new Fill({
            color: 'rgba(0, 0, 255, 0.1)'
          })
        })
      ]
    });
    // const top : topology = []
    for(let i=0; i < 1000; i++) {
      let coord = this.turfService.randomPointInPolygon(this.turfService.geoJsonObject.features[0]).geometry.coordinates
      vectorSource.addFeature(new Feature(new Circle(fromLonLat(coord, 'EPSG:3857'), 20)));
    }

    const geojsonFormat = new GeoJSON();

    const features = geojsonFormat.readFeatures(this.turfService.geoJsonObject, {
      featureProjection: 'EPSG:3857',
    });

    vectorSource.addFeatures(features)

    this.map.addLayer(layer);
  }
}
