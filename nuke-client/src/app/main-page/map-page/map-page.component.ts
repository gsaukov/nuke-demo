import {Component, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA, OnInit} from '@angular/core';
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

    const vectorSource = new VectorSource();

    const layer = new Vector({
      source: vectorSource,
      style: [
        new Style({
          stroke: new Stroke({
            color: 'blue',
            width: 3
          })
        })
      ]
    });

    for(let i=0; i < 10; i++) {
      let coord = this.turfService.randomPointInPolygon(this.turfService.geoJsonObject.features[0]).geometry.coordinates
      const circleFeature = new Feature(new Circle(fromLonLat(coord, 'EPSG:3857'), 2000));
      circleFeature.setStyle(this.radialGraientStylre());
      vectorSource.addFeature(circleFeature);
    }

    const geojsonFormat = new GeoJSON();
    const features = geojsonFormat.readFeatures(this.turfService.geoJsonObject, {
      featureProjection: 'EPSG:3857',
    });

    vectorSource.addFeatures(features)

    this.map.addLayer(layer);
  }

  private radialGraientStylre():Style{
    return new Style({
      renderer(coordinates:any, state:any) {
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
