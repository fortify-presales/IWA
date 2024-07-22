import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from 'src/app/_services/product.service';
import { Product } from 'src/app/models/product';

@Component({
  selector: 'app-new-products',
  templateUrl: './new-products.component.html',
  styleUrls: ['./new-products.component.css']
})
export class NewProductsComponent implements OnInit {
  @Input() page: number = 1;
  @Input() limit: number = 3;
  products!: Product[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService
  ) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts(this.page, this.limit)
      .subscribe(products => this.products = products);
  }

  gotoProducts(product: Product) {
    const productId = product ? product.id : null;
    // Pass along the product id if available
    // so that the ProductList component can select that hero.
    // Include a junk 'foo' property for fun.
    this.router.navigate(['/products', { id: productId, foo: 'foo' }]);
  }

}