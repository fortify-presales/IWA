import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Product } from '../models/product';
import { MessageService } from './message.service';


@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private productsUrl = '/api/v3/products';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }

  /** GET products from the server */
  getProducts(page: number, limit: number): Observable<Product[]> {
    const url = `${this.productsUrl}`;
    return this.http.get<Product[]>(url, { params: new HttpParams({fromString: `page=${page}&limit=${limit}`}) })
      .pipe(
        tap(_ => this.log('fetched products')),
        catchError(this.handleError<Product[]>('getProducts', []))
      );
  }

  /** GET product by id. Return `undefined` when id not found */
  getProductNo404<Data>(id: string): Observable<Product> {
    const url = `${this.productsUrl}/?id=${id}`;
    return this.http.get<Product[]>(url)
      .pipe(
        map(products => products[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? `fetched` : `did not find`;
          this.log(`${outcome} product id=${id}`);
        }),
        catchError(this.handleError<Product>(`getProduct id=${id}`))
      );
  }

  /** GET product by id. Will 404 if id not found */
  getProduct(id: string): Observable<Product> {
    const url = `${this.productsUrl}/${id}`;
    return this.http.get<Product>(url).pipe(
      tap(_ => this.log(`fetched product id=${id}`)),
      catchError(this.handleError<Product>(`getProduct id=${id}`))
    );
  }

  /* GET products whose name contains search term */
  searchProducts(keywords: string, limit: number): Observable<Product[]> {
    var url = `${this.productsUrl}/?keywords=${keywords}`;
    if (limit > 0) {
      url = `${this.productsUrl}/?keywords=${keywords}&limit=${limit}`;
    }
    return this.http.get<Product[]>(url).pipe(
      tap(x => x.length ?
        this.log(`found ${x.length} products matching "${keywords}"`) :
        this.log(`no products matching "${keywords}"`)),
      catchError(this.handleError<Product[]>('searchProducts', []))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a ProductService message with the MessageService */
  private log(message: string) {
    // this.messageService.add(`ProductService: ${message}`);
    console.log(`ProductService: ${message}`);
  }

}
