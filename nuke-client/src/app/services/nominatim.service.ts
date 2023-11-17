import {Injectable} from "@angular/core";
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";

const API_URL = 'https://nominatim.openstreetmap.org'

@Injectable({
  providedIn: 'root'
})
export class NominatimService {

  private l = [{
    "place_id": 221991999,
    "licence": "Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright",
    "osm_type": "relation",
    "osm_id": 2216724,
    "lat": "41.3123363",
    "lon": "69.2787079",
    "class": "boundary",
    "type": "administrative",
    "place_rank": 7,
    "importance": 0.6085809707997437,
    "addresstype": "city",
    "name": "Tashkent",
    "display_name": "Tashkent, 100000, Uzbekistan",
    "boundingbox": ["41.1634205", "41.4224955", "69.1217101", "69.4769671"]
  }, {
    "place_id": 221449565,
    "licence": "Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright",
    "osm_type": "relation",
    "osm_id": 196251,
    "lat": "41.0496815",
    "lon": "69.3711365",
    "class": "boundary",
    "type": "administrative",
    "place_rank": 8,
    "importance": 0.4386504519870678,
    "addresstype": "state",
    "name": "Tashkent Region",
    "display_name": "Tashkent Region, Uzbekistan",
    "boundingbox": ["40.1878119", "42.2950775", "68.6403992", "71.2690927"]
  }]

  constructor(private http: HttpClient) {
  }

  getCityData(cityName: string): Observable<any> {
    return this.http.get<any>(`${API_URL}/search?X-Requested-With=overpass-turbo&format=json&q=${cityName}`)
  }

}
