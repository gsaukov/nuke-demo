import {Component, Input} from '@angular/core';
import Map from 'ol/Map';

@Component({
  selector: 'app-calculator',
  templateUrl: './calculator.component.html',
  styleUrls: ['./calculator.component.css']
})
export class CalculatorComponent {

  @Input() map!: Map;


  constructor() {
  }
}
