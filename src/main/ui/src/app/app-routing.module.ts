import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from "./_helpers/auth.guard";

const homeModule = () => import('./home/home.module').then(x => x.HomeModule);
const accountModule = () => import('./account/account.module').then(x => x.AccountModule);
const profileModule = () => import('./profile/profile.module').then(x => x.ProfileModule);
const productModule = () => import('./product/product.module').then(x => x.ProductModule);
const cartModule = () => import('./cart/cart.module').then(x => x.CartModule);

const routes: Routes = [
  { path: '', loadChildren: homeModule },
  { path: 'account', loadChildren: accountModule },
  { path: 'profile', loadChildren: profileModule, canActivate: [AuthGuard] },
  { path: 'products', loadChildren: productModule },
  { path: 'cart', loadChildren: cartModule},
  // otherwise redirect to home - or TODO: pageNotFound ?
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(
    routes,
    { enableTracing: false } // for debugging only
  )],
  exports: [RouterModule]
})
export class AppRoutingModule { }
