import {CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {MainPageComponent} from './main-page/main-page.component';
import {MapPageComponent} from './main-page/map-page/map-page.component';
import {HttpClientModule} from "@angular/common/http";
import { CalculatorComponent } from './main-page/map-page/calculator/calculator.component';

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    MapPageComponent,
    CalculatorComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA,
    NO_ERRORS_SCHEMA
  ]
})
export class AppModule {
}
