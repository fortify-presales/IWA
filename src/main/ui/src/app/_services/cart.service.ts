import {EventEmitter, Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  // representation of shopping cart in localStorage
  localStorage: Storage;

  // number of items in cart
  public cartCount$: EventEmitter<number>;

  private cart: CartItem[] = [];
  private cartCount: number = 0;

  constructor() {
    this.localStorage = window.localStorage;
    if (localStorage.getItem('cart')) {
      try {
        this.cart = JSON.parse(localStorage.getItem('cart'));
      } catch (e) {
        console.log("Error retrieving cart from localStorage");
        localStorage.removeItem('cart');
      }
    }
    this.cartCount$ = new EventEmitter();
  }

  public getCount(): number {
    return this.updateCount();
  }

  public get(): CartItem[] {
    return this.cart;
  }

  public add(id: string, quantity: number): CartItem[] {
    let newItem = {
      id: id,
      quantity: quantity,
      image: '',
      name: '',
      price: 0,
      total: 0
    };
    const index = this.cart.findIndex(x => x.id === id);
    if (index >= 0) {
      this.cart.splice(index, 1);
    }
    this.cart.push(newItem);
    this.save();
    return this.cart;
  }

  public replace(id: string, quantity: number): CartItem[] {
    const index = this.cart.findIndex(x => x.id === id);
    if (index >= 0) {
      let existingItem = this.cart[index];
      existingItem.quantity = quantity;
      this.cart[index] = existingItem;
    }
    this.save();
    return this.cart;
  }

  public remove(id: string): CartItem[] {
    const index = this.cart.findIndex(x => x.id === id);
    if (index >= 0) {
      this.cart.splice(index, 1);
    }
    this.save();
    return this.cart;
  }

  private save(): void {
    const parsed = JSON.stringify(this.cart);
    localStorage.setItem('cart', parsed);
    this.updateCount();
  }

  private updateCount(): number {
    this.cartCount = this.cart.reduce((sum, item) => sum + Number(item.quantity), 0);
    this.cartCount$.emit(this.cartCount);
    return this.cartCount;
  }

  get isLocalStorageSupported(): boolean {
    return !!this.localStorage
  }

}

export class CartItem {
  public id: string;
  public quantity: number = 0;
  public image: string = '';
  public name: string = '';
  public price: number = 0;
  public total: number = 0;
}
