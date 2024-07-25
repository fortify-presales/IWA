import { Component, OnInit } from '@angular/core';
import { Product } from '../models/product';
import { debounceTime, distinctUntilChanged, Observable, startWith, Subject, switchMap } from 'rxjs';

import { ProductService } from '../_services/product.service';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  title = 'IWA Pharmacy Direct :: Products';
  limit = 10;
  products$!: Observable<Product[]>;
  totalCount!: number;
  keywords = new Subject<string>();
  keywordsInput!: string;

  constructor( private route: ActivatedRoute, 
               private productService: ProductService) { }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      const keywordsParam = params.get('keywords');
      if (keywordsParam != null) {
        this.keywordsInput = keywordsParam;
        this.search(keywordsParam);
      }
    });
    this.totalCount = 0; // TBD: total number of products matched
    this.getProducts();
  }

  search(keyword: string): void {
    this.keywords.next(keyword);
  }

  getProducts(): void {
    this.products$ = this.keywords.pipe(
      startWith(this.keywordsInput == undefined ? '' : this.keywordsInput),

      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new search term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the search term changes
      switchMap(
        (term: string) => this.productService.searchProducts(term, this.limit)
      ),
    );
  }

}
