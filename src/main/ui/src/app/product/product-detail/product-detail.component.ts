import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { ProductService } from "../../_services/product.service";
import { Product } from "../../_models/product";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  product$: Observable<Product>;

  constructor( private route: ActivatedRoute,
               private router: Router,
               private service: ProductService) { }

  ngOnInit() {
    this.product$ = this.route.paramMap.pipe(
      switchMap((params: ParamMap) =>
        this.service.getProduct(params.get('id')))
    );
  }

  gotoProducts(product: Product) {
    const productId = product ? product.id : null;
    this.router.navigate(['/products', { id: productId }]);
  }

  range(start: number, end: number) {
    return [...Array(end-start).keys()].map(i => i + start);
  }

}
