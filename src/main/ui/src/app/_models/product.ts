export interface Product {
  id: string;
  code: string;
  name: string;
  summary: string;
  description: string;
  image: string;
  price: number;
  onSale: boolean;
  salePrice: number;
  inStock: boolean;
  timeToStock: number;
  rating: number;
  available: boolean;
}
