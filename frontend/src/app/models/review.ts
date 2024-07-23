import { Account } from "./account";
import { Product } from "./product";

export interface Review {
    id: string;
    reviewDate: Date;
    comment: string;
    rating: number;
    product: Product;
    user: Account;
  }