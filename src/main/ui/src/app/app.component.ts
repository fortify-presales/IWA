import { Component } from '@angular/core';

import { Account} from "./_models/account";
import { Role } from "./_models/role";
import { AccountService } from "./_services/account.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  Role = Role;
  account: Account;
  title = 'IWA Pharmacy Direct';

  constructor(private accountService: AccountService) {
    this.accountService.account.subscribe(x => this.account = x);
  }

  logout() {
    this.accountService.logout();
  }

}
