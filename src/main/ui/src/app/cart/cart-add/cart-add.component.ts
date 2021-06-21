import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router} from "@angular/router";

import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

import { CartService } from "../../_services/cart.service";

@Component({
  selector: 'app-shopping-cart-add',
  templateUrl: './cart-add.component.html',
  styleUrls: ['./cart-add.component.css']
})
export class CartAddComponent implements OnInit {
  @Input() pid: string;
  @Input() inStock: boolean = true;

  @Output() quantity: number = 1;

  closeResult: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal,
    private cartService: CartService
  ) { }

  ngOnInit(): void { }

  incrementQuantity() {
    this.quantity++;
  }

  decrementQuantity() {
    if (this.quantity > 0) this.quantity--;
  }

  quantityTextEntered(event: any) {
    this.quantity = event.target.value;
  }

  addToCart(content) {
    this.cartService.add(this.pid, this.quantity);
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      if (result === "buy") {
        this.router.navigate( ['/cart'])
      } else if (result === "continue") {
        this.router.navigate(['/products', { }]);
      }
    }, (reason) => {
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

}
