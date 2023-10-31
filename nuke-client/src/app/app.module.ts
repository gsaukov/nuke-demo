import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { MainPageComponent } from './main-page/main-page.component';
import { MapPageComponent } from './main-page/map-page/map-page.component';

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    MapPageComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
