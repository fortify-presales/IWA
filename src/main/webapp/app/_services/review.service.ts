import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Review } from '../models/review';
import { MessageService } from './message.service';


@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private ReviewsUrl = '/api/v3/reviews';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }

  /** GET Reviews from the server */
  getReviews(pid: string, page: number, limit: number): Observable<Review[]> {
    const url = `${this.ReviewsUrl}`;
    return this.http.get<Review[]>(url, { params: new HttpParams({fromString: `pid=${pid}&page=${page}&limit=${limit}`}) })
      .pipe(
        tap(_ => this.log('fetched Reviews')),
        catchError(this.handleError<Review[]>('getReviews', []))
      );
  }

  /** GET Review by id. Return `undefined` when id not found */
  getReviewNo404<Data>(id: string): Observable<Review> {
    const url = `${this.ReviewsUrl}/${id}`;
    return this.http.get<Review[]>(url)
      .pipe(
        map(Reviews => Reviews[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? `fetched` : `did not find`;
          this.log(`${outcome} Review id=${id}`);
        }),
        catchError(this.handleError<Review>(`getReview id=${id}`))
      );
  }

  /** GET Review by id. Will 404 if id not found */
  getReview(id: string): Observable<Review> {
    const url = `${this.ReviewsUrl}/${id}`;
    return this.http.get<Review>(url).pipe(
      tap(_ => this.log(`fetched Review id=${id}`)),
      catchError(this.handleError<Review>(`getReview id=${id}`))
    );
  }

  /* GET Reviews whose name contains search term */
  searchReviews(keywords: string, limit: number): Observable<Review[]> {
    var url = `${this.ReviewsUrl}/?keywords=${keywords}`;
    if (limit > 0) {
      url = `${this.ReviewsUrl}/?keywords=${keywords}&limit=${limit}`;
    }
    return this.http.get<Review[]>(url).pipe(
      tap(x => x.length ?
        this.log(`found ${x.length} Reviews matching "${keywords}"`) :
        this.log(`no Reviews matching "${keywords}"`)),
      catchError(this.handleError<Review[]>('searchReviews', []))
    );
  }

  /* GET count of Review for Product */
  getReviewCount(id: string): Observable<Review[]> {
    const url = `${this.ReviewsUrl}?pid=${id}`;
    return this.http.get<Review[]>(url).pipe(
      tap(x => x.length ?
        this.log(`found ${x.length} Reviews`) :
        this.log(`no Reviews`)),
      catchError(this.handleError<Review[]>('getReviewCount', []))
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

  /** Log a ReviewService message with the MessageService */
  private log(message: string) {
    // this.messageService.add(`ReviewService: ${message}`);
    console.log(`ReviewService: ${message}`);
  }

}
