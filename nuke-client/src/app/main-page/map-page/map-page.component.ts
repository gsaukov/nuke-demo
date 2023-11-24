import {Component, OnInit} from '@angular/core';
import Map from 'ol/Map';
import {MapService} from "../../services/map.service";

@Component({
  selector: 'app-map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.css']
})
export class MapPageComponent implements OnInit {

  private _map!: Map;

  constructor(private mapService: MapService) {
  }

  ngOnInit(): void {
    this._map = this.mapService.buildMap(this._map);
  }

  get map(): Map {
    return this._map;
  }
}
