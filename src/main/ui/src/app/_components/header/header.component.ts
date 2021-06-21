import { Component, OnInit } from '@angular/core';

import { AccountService } from "../../_services/account.service";
import { Account } from "../../_models/account"
import { Role } from "../../_models/role"
import { environment } from "../../../environments/environment";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  Role = Role;
  account: Account;
  cartCount:number = 0;
  apiUrl = environment.apiUrl;

  constructor(private accountService: AccountService) {
    this.accountService.account.subscribe(x => this.account = x);
  }

  ngOnInit(): void {  }

  cartCountChange(count: number) {
    this.cartCount = count;
  }

}
