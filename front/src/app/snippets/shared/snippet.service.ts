import { BaseService } from '../../shared/base.service';
import { Observable } from 'rxjs/Observable';
import { Snippet } from './snippet';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';

@Injectable()
export class SnippetService extends BaseService {

  snippetEndpoint = '/snippet';

  constructor(private http: HttpClient) {
    super();
  }

  getSnippets(): Observable<Snippet[]> {
    return this.http.get<Snippet[]>(this.buildUrl(this.snippetEndpoint))
      .pipe(
        catchError(this.extractError)
      );
  }

  getSnippet(id: string): Observable<Snippet> {
    return this.http.get<Snippet>(this.buildUrl(this.snippetEndpoint) + '/' + id)
      .pipe(
        catchError(this.extractError)
      );
  }

  createSnippet(snippet: Snippet): Observable<Snippet> {
    return this.http.post<Snippet>(this.buildUrl(this.snippetEndpoint), snippet)
      .pipe(
        catchError(this.extractError)
      );
  }
}
