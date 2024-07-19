import { Component, OnInit } from '@angular/core';
import { StorageService } from '../_services/storage.service';
import { Router } from '@angular/router';

// TBD: implement authguard
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: any;

  constructor(private router: Router, private storageService: StorageService) { }

  ngOnInit(): void {
    if (!this.storageService.isLoggedIn()) {
      this.router.navigate(['login']);
    }
    this.currentUser = this.storageService.getUser();
  }
}
