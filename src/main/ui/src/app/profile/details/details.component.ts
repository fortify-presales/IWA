import { Component } from '@angular/core';
import { AccountService } from "../../_services/account.service";

@Component({ templateUrl: 'details.component.html' })
export class DetailsComponent {
    account = this.accountService.accountValue;

    constructor(private accountService: AccountService) { }

    ngOnInit() {
      this.accountService.getById(this.account.id).subscribe(x => this.account = x);
    }
}
