import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { ReviewService } from '../../_services/review.service';
import { Review } from '../../models/review';

@Component({
  selector: 'app-product-reviews',
  templateUrl: './product-reviews.component.html',
  styleUrls: ['./product-reviews.component.css']
})
export class ProductReviewsComponent implements OnInit {
  @Input() pid!: string;
  @Input() page: number = 1;
  @Input() limit: number = 10;
  reviews!: Review[];

  constructor(private reviewService: ReviewService) { }

    ngOnInit(): void {
      this.getReviews();
    }

    getReviews(): void {
      this.reviewService.getReviews(this.pid, this.page, this.limit)
        .subscribe(reviews => this.reviews = reviews);
    }
}
