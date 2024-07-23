import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductReviewsComponent } from './product-reviews/product-reviews.component';
import { ProductReviewCountComponent } from './product-review-count/product-review-count.component';



@NgModule({
  declarations: [
    ProductReviewsComponent,
    ProductReviewCountComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    ProductReviewsComponent,
    ProductReviewCountComponent
  ]
})
export class ReviewsModule { }
