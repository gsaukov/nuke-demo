import {Component, Input} from '@angular/core';
import Map from 'ol/Map';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-calculator',
  templateUrl: './calculator.component.html',
  styleUrls: ['./calculator.component.css']
})
export class CalculatorComponent {

  @Input() map!: Map;
  form: FormGroup


  constructor() {
    this.form = new FormGroup({
      start: new FormControl(null, [Validators.required]),
      end: new FormControl(null, [Validators.required]),
    })
  }

  onSubmit() {}
}
