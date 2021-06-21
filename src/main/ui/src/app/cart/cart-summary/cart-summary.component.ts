import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-layout',
  templateUrl: './cart-summary.component.html',
  styleUrls: ['./cart-summary.component.css']
})
export class CartSummaryComponent implements OnInit {
  title = 'IWA Pharmacy Direct :: Cart';

  constructor() { }

  ngOnInit(): void {

  }

}
