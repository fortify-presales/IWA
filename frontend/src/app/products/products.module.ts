import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductsComponent } from './products.component';
import { NewProductsComponent } from './new-products/new-products.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { ReviewsModule } from "../reviews/reviews.module";


@NgModule({
  declarations: [
    ProductsComponent,
    NewProductsComponent,
    ProductDetailsComponent
  ],
  imports: [
    CommonModule,
    ProductsRoutingModule,
    ReviewsModule
],
  exports: [
    NewProductsComponent
  ]
})
export class ProductsModule { }
