import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, finalize } from 'rxjs/operators';

import { Account } from "../_models/account"
import { environment } from "../../environments/environment";
import { LocalStorageService } from "./localstorage.service";
import {JWTTokenService} from "./jwt-token.service";
import * as moment from 'moment';

//const baseUrl = `${environment.apiUrl}/auth`;
const baseUrl = '/api';

@Injectable({ providedIn: 'root' })
export class AccountService {
    private accountSubject: BehaviorSubject<Account>;
    public account: Observable<Account>;

    constructor(
        private router: Router,
        private http: HttpClient,
        private localStorageService: LocalStorageService,
        private jwtTokenService: JWTTokenService
    ) {
        this.accountSubject = new BehaviorSubject<Account>(null);
        this.account = this.accountSubject.asObservable();
    }

    public get accountValue(): Account {
        return this.accountSubject.value;
    }

    login(username: string, password: string) {
        return this.http.post<any>(`${baseUrl}/authentication/sign-in`, { username, password }, { withCredentials: true })
            .pipe(map(account => {
                localStorage.setItem("accessToken", account.accessToken);
                localStorage.setItem("tokenExpiration", account.expiration);
                this.accountSubject.next(account);
                this.startRefreshTokenTimer();
                return account;
            }));
    }

    logout() {
        //this.http.post<any>(`${baseUrl}/revoke-token`, {}, { withCredentials: true }).subscribe();
        this.stopRefreshTokenTimer();
        this.accountSubject.next(null);
        this.router.navigate(['/account/login']);
    }

    refreshToken() {
      let accessToken = localStorage.getItem("accessToken");
      this.jwtTokenService.setToken(accessToken);
      return this.http.post<any>(`${baseUrl}/users/refresh-token`, {}, { withCredentials: true })
          .pipe(map((account) => {
              this.accountSubject.next(account);
              this.startRefreshTokenTimer();
              return account;
          }));
    }

    register(account: Account) {
        return this.http.post(`${baseUrl}/site/register`, account);
    }

    verifyEmail(token: string) {
        return this.http.post(`${baseUrl}/site/verify-email`, { token });
    }

    forgotPassword(email: string) {
        return this.http.post(`${baseUrl}/site/forgot-password`, { email });
    }

    validateResetToken(token: string) {
        return this.http.post(`${baseUrl}/site/validate-reset-token`, { token });
    }

    resetPassword(token: string, password: string, confirmPassword: string) {
        return this.http.post(`${baseUrl}/site/reset-password`, { token, password, confirmPassword });
    }

    getAll() {
        return this.http.get<Account[]>(`${baseUrl}/users`);
    }

    getById(id: string) {
        return this.http.get<Account>(`${baseUrl}/users/${id}`);
    }

    create(params) {
        return this.http.post(`${baseUrl}/users`, params);
    }

    update(id, params) {
        return this.http.put(`${baseUrl}/users/${id}`, params)
            .pipe(map((account: any) => {
                // update the current account if it was updated
                if (account.id === this.accountValue.id) {
                    // publish updated account to subscribers
                    account = { ...this.accountValue, ...account };
                    this.accountSubject.next(account);
                }
                return account;
            }));
    }

    delete(id: string) {
        return this.http.delete(`${baseUrl}/users/${id}`)
            .pipe(finalize(() => {
                // auto logout if the logged in account was deleted
                if (id === this.accountValue.id)
                    this.logout();
            }));
    }

    // helper methods

    private refreshTokenTimeout;

    private startRefreshTokenTimer() {
        // parse json object from base64 encoded jwt token
        const jwtToken = JSON.parse(atob(this.accountValue.accessToken.split('.')[1]));

        // set a timeout to refresh the token a minute before it expires
        const expires = new Date(jwtToken.exp * 1000);
        const timeout = expires.getTime() - Date.now() - (60 * 1000);
        this.refreshTokenTimeout = setTimeout(() => this.refreshToken().subscribe(), timeout);
    }

    private stopRefreshTokenTimer() {
        clearTimeout(this.refreshTokenTimeout);
    }
}
