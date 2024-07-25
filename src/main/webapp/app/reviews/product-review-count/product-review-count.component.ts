import { Component, Input, OnInit } from '@angular/core';
import { ReviewService } from '../../_services/review.service';

@Component({
  selector: 'app-product-review-count',
  templateUrl: './product-review-count.component.html',
  styleUrls: ['./product-review-count.component.css']
})
export class ProductReviewCountComponent implements OnInit {
  @Input() pid!: string;
  reviewCount: number = 0;

  constructor(private reviewService: ReviewService) { }
  
  ngOnInit(): void {
    this.getReviewCount();
  }

  getReviewCount(): void {
    this.reviewService.getReviewCount(this.pid)
      .subscribe(reviews => this.reviewCount = reviews.length);
  }

}
