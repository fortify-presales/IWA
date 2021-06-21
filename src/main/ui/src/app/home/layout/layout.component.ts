import { Component, OnInit } from '@angular/core';

import { AccountService } from "../../_services/account.service";

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit {
  title = 'IWA Pharmacy Direct';
  account = this.accountService.accountValue;
  loggedIn = false;

  constructor(private accountService: AccountService) { }

  ngOnInit(): void {
    if (this.account) this.loggedIn = true;
  }

}
