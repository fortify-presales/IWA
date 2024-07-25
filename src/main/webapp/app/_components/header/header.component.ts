import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from '../../_services/auth.service';
import { StorageService } from '../../_services/storage.service';
import { EventBusService } from '../../_shared/event-bus.service';
import { EventData } from '../../_shared/event.class';
import { Account } from '../../models/account';
import { Role } from '../../models/role';


//import { AccountService } from '../../_services/account.service';
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
  apiDocsUrl = "/swagger-ui/index.html?url=/v3/api-docs";

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
