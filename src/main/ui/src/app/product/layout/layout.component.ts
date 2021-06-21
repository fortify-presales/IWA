import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
  debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';

import { Product } from "../../_models/product";
import { ProductService } from "../../_services/product.service";

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit {
  title = 'IWA Pharmacy Direct :: Products';
  limit = 10;
  products$: Observable<Product[]>;
  productCount: number
  keywords = new Subject<string>()

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.getProducts();
  }

  search(keyword: string): void {
    this.keywords.next(keyword);
  }

  getProducts(): void {
    this.products$ = this.keywords.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap(
        (term: string) => this.productService.searchProducts(term, this.limit)
      ),
    );
    //this.productService.getProducts(this.limit)
    //  .subscribe(products => this.products = products);
  }

}
