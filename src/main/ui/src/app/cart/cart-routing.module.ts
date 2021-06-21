import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CartViewComponent } from './cart-view/cart-view.component';
import { CartCheckoutComponent } from "./cart-checkout/cart-checkout.component";
import { CartSummaryComponent } from './cart-summary/cart-summary.component';

const routes: Routes = [
  { path: '', component: CartViewComponent },
  { path: 'checkout', component: CartCheckoutComponent },
  { path: 'summary', component: CartSummaryComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CartRoutingModule { }
