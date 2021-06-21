import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';

import { CartService } from "../../_services/cart.service";

@Component({
  selector: 'app-shopping-cart-count',
  templateUrl: './cart-count.component.html',
  styleUrls: ['./cart-count.component.css']
})
export class CartCountComponent implements OnInit {

  @Input()
  count: number = 0;

  @Output()
  change = new EventEmitter<number>();

  constructor(
    private cartService: CartService) {
    cartService.cartCount$.subscribe(count => this.onCountUpdated(count));
  }

  ngOnInit(): void {
    this.count = this.cartService.getCount();
  }

  updateCartCount(event: any) {
    this.count = event.target.value;
  }

  private onCountUpdated(count: number): void {
    this.count = count;
  }

}
