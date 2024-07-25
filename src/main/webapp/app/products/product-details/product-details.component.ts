import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Observable, switchMap } from 'rxjs';
import { ProductService } from '../../_services/product.service';
import { Product } from '../../models/product';


@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {
  product$!: Observable<Product>;

  constructor( private route: ActivatedRoute,
               private router: Router,
               private service: ProductService) { }

  ngOnInit() {
    const id = String(this.route.snapshot.paramMap.get('id'));
    if (id) {
      console.log("id=%s", id)
      this.product$ = this.service.getProduct(id);
    }
    /*this.product$ = this.route.paramMap.pipe(
      switchMap((params: ParamMap) =>
        this.service.getProduct(params.get('id') != null ?
          params.get('id') : ''))
    );*/
  }

  gotoProducts(product: Product) {
    const productId = product ? product.id : null;
    this.router.navigate(['/products', { id: productId }]);
  }

  range(start: number, end: number) {
    return [...Array(end-start).keys()].map(i => i + start);
  }

}
