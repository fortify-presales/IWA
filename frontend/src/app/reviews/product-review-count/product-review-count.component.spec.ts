import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductReviewCountComponent } from './product-review-count.component';

describe('ProductReviewCountComponent', () => {
  let component: ProductReviewCountComponent;
  let fixture: ComponentFixture<ProductReviewCountComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductReviewCountComponent]
    });
    fixture = TestBed.createComponent(ProductReviewCountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
