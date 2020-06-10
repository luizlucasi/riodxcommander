import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRadio } from 'app/shared/model/radio.model';

type EntityResponseType = HttpResponse<IRadio>;
type EntityArrayResponseType = HttpResponse<IRadio[]>;

@Injectable({ providedIn: 'root' })
export class RadioService {
  public resourceUrl = SERVER_API_URL + 'api/radios';

  constructor(protected http: HttpClient) {}

  create(radio: IRadio): Observable<EntityResponseType> {
    return this.http.post<IRadio>(this.resourceUrl, radio, { observe: 'response' });
  }

  update(radio: IRadio): Observable<EntityResponseType> {
    return this.http.put<IRadio>(this.resourceUrl, radio, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRadio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRadio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
