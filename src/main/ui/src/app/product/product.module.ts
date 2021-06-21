import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductRoutingModule } from "./product-routing.module";
import { CartModule } from "../cart/cart.module";

import { LayoutComponent } from './layout/layout.component';
import { NewProductsComponent } from "./new-products/new-products.component";

@NgModule({
  declarations: [
    LayoutComponent,
    NewProductsComponent
  ],
  imports: [
    CommonModule,
    ProductRoutingModule,
    CartModule
  ],
  exports: [
    NewProductsComponent
  ]
})
export class ProductModule { }
