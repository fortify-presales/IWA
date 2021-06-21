import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { AccountService } from "../../_services/account.service";
import { AlertService } from "../../_services/alert.service";
import { MustMatch } from "../../_helpers/must-match.validator";

@Component({ templateUrl: 'update.component.html' })
export class UpdateComponent implements OnInit {
    account = this.accountService.accountValue;
    form: FormGroup;
    loading = false;
    submitted = false;
    deleting = false;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private accountService: AccountService,
        private alertService: AlertService
    ) { }

    ngOnInit() {
      this.accountService.getById(this.account.id).subscribe(x => {
        this.account = x;
        this.form = this.formBuilder.group({
              username: [this.account.username],
              firstName: [this.account.firstName, Validators.required],
              lastName: [this.account.lastName, Validators.required],
              email: [this.account.email, [Validators.required, Validators.email]],
              phone: [this.account.phone, Validators.required],
              address: [this.account.address, Validators.required],
              city: [this.account.city, Validators.required],
              state: [this.account.state, Validators.required],
              zip: [this.account.zip, Validators.required],
              country: [this.account.country, Validators.required],
              password: ['', [Validators.minLength(6)]],
              confirmPassword: ['']
          }, {
              validator: MustMatch('password', 'confirmPassword')
          })
      });
    }

    // convenience getter for easy access to form fields
    get f() { return this.form.controls; }

    onSubmit() {
        this.submitted = true;

        // reset alerts on submit
        this.alertService.clear();

        // stop here if form is invalid
        if (this.form.invalid) {
            return;
        }

        this.loading = true;
        this.accountService.update(this.account.id, this.form.value)
            .pipe(first())
            .subscribe({
                next: () => {
                    this.alertService.success('Update successful', { keepAfterRouteChange: true });
                    this.router.navigate(['../'], { relativeTo: this.route });
                },
                error: error => {
                    this.alertService.error(error);
                    this.loading = false;
                }
            });
    }

    onDelete() {
        if (confirm('Are you sure?')) {
            this.deleting = true;
            this.accountService.delete(this.account.id)
                .pipe(first())
                .subscribe(() => {
                    this.alertService.success('Account deleted successfully', { keepAfterRouteChange: true });
                });
        }
    }
}
