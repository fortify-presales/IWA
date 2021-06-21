import {Component, Input, OnInit, SimpleChanges} from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";

import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

import { CartItem, CartService } from "../../_services/cart.service";
import { ProductService } from "../../_services/product.service";
import {Product} from "../../_models/product";

@Component({
  selector: 'app-layout',
  templateUrl: './cart-view.component.html',
  styleUrls: ['./cart-view.component.css']
})
export class CartViewComponent implements OnInit {
  title = 'IWA Pharmacy Direct :: Shopping Cart';

  cartIsEmpty: boolean = true;
  cart: CartItem[] = [];
  subTotal: number = 0;
  total: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal,
    private cartService: CartService,
    private productService: ProductService
  ) { }

  ngOnInit(): void {
    let lsCart = this.cartService.get();
    // get the latest product details from the database
    // in case price has changed, out of stock etc.
    const size = Object.keys(lsCart).length;
    if (size > 0) {
      this.cartIsEmpty = false;
      lsCart.forEach(obj => {
        this.productService.getProduct(obj.id).subscribe(
          response => {
            const responseItem = response;
            let price = responseItem.price;
            if (responseItem.onSale) {
              price = responseItem.salePrice;
            }
            const newItem = {
              id: obj.id,
              quantity: obj.quantity,
              image: responseItem.image,
              name: responseItem.name,
              price: price,
              total: Number(price * (obj.quantity))

            };
            this.subTotal += newItem.total;
            this.total += newItem.total;
            this.cart.push(newItem);
          }
        )
      })
    }
  }

  ngOnChanges(changes: SimpleChanges) {

  }

  getItemQuantity(itemId: string): number {
    let item = this.cart.find(x => x.id === itemId);
    return Number(item.quantity);
  }

  setItemQuantity(itemId: string, quantity: number) {
    const index = this.cart.findIndex(x => x.id === itemId);
    let item = this.cart.find(x => x.id === itemId);
    item.quantity = quantity;
    item.total = Number(item.quantity * item.price)
    this.cartService.replace(itemId, quantity);
    this.updateTotals();
  }

  incrementQuantity(itemId: string) {
    let quantity = this.getItemQuantity(itemId);
    this.setItemQuantity(itemId, Number(quantity + 1));
  }

  decrementQuantity(itemId: string) {
    let quantity = this.getItemQuantity(itemId);
    if (quantity > 0) {
      this.setItemQuantity(itemId, Number(quantity - 1));
    }
  }

  quantityTextEntered(event: any, itemId: string) {
    let quantity = event.target.value;
    if (quantity >= 0) {
      this.setItemQuantity(itemId, quantity);
    }
  }

  removeItem(itemId: string) {
    this.cart = this.cartService.remove(itemId);
    this.cartIsEmpty = !(this.cart.length > 0);
    this.updateTotals();
  }

  updateTotals() {
    this.subTotal = this.cart.reduce((sum, item) => sum + Number(item.price * item.quantity), 0);
    // TODO: calculate any discounts
    this.total = this.subTotal;
  }

  continueShopping() {
    this.router.navigate(['/products', { }]);
  }

  checkout() {
    this.router.navigate( ['/cart/checkout'])
  }

}
