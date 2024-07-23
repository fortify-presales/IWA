import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductReviewsComponent } from './product-reviews.component';

describe('ProductReviewsComponent', () => {
  let component: ProductReviewsComponent;
  let fixture: ComponentFixture<ProductReviewsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductReviewsComponent]
    });
    fixture = TestBed.createComponent(ProductReviewsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
