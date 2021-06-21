import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartCountComponent } from './cart-count.component';

describe('ShoppingCartCountComponent', () => {
  let component: CartCountComponent;
  let fixture: ComponentFixture<CartCountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CartCountComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CartCountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
