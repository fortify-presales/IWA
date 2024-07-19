import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/_services/auth.service';
import { StorageService } from 'src/app/_services/storage.service';
import { EventBusService } from 'src/app/_shared/event-bus.service';
import { EventData } from 'src/app/_shared/event.class';

import { Account } from 'src/app/models/account';

//import { AccountService } from '../../_services/account.service';
import { Role } from 'src/app/models/role';
//import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  isLoggedIn = false;
  username?: string;

  eventBusSub?: Subscription;

  role = Role.User;
  //Role: role;
  account = new Account();
  //account: Account;
  cartCount = 0;
  //apiUrl = environment.apiUrl;
  apiDocsUrl = "";

  //constructor(private accountService: AccountService) {
  //  this.accountService.account.subscribe(x => this.account = x);
  //}

  constructor(
    private storageService: StorageService,
    private authService: AuthService,
    private eventBusService: EventBusService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();
    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      //this.roles = user.roles;
      this.username = user.username;
    }
  }

  cartCountChange(count: number) {
    this.cartCount = count;
  }

  logout(): void {
    this.eventBusService.emit(new EventData('logout', null));
  };
}
