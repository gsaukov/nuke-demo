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
      cityName: new FormControl(null, [Validators.required]),
      radius: new FormControl(200, [Validators.required]),
      number: new FormControl(5, [Validators.required]),
    })
  }

  onSubmit() {
    const cityName = this.form.controls['cityName'].value
    const radius = this.form.controls['radius'].value
    const number = this.form.controls['number'].value
    console.log(cityName + ' ' + radius + ' ' + number)
  }
}
