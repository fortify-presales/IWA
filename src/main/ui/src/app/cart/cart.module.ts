import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms";

import { CartRoutingModule } from "./cart-routing.module";
import { CartCountComponent } from "./cart-count/cart-count.component";
import { CartAddComponent } from "./cart-add/cart-add.component";
import { CartViewComponent } from './cart-view/cart-view.component';
import { CartCheckoutComponent } from "./cart-checkout/cart-checkout.component";
import { CartSummaryComponent } from './cart-summary/cart-summary.component';

@NgModule({
  declarations: [
    CartCountComponent,
    CartAddComponent,
    CartViewComponent,
    CartCheckoutComponent,
    CartSummaryComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    CartRoutingModule,
  ],
  exports: [
    CartCountComponent,
    CartAddComponent
  ]
})
export class CartModule { }
