import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from "./home-routing.module";
import { LayoutComponent } from './layout/layout.component';
import { ProductModule } from "../product/product.module";

@NgModule({
  declarations: [
    LayoutComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    ProductModule
  ]
})
export class HomeModule { }
