import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewProductsComponent } from './new-products.component';

describe('NewProductsComponent', () => {
  let component: NewProductsComponent;
  let fixture: ComponentFixture<NewProductsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewProductsComponent]
    });
    fixture = TestBed.createComponent(NewProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
