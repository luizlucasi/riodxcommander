import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IModem } from 'app/shared/model/modem.model';

type EntityResponseType = HttpResponse<IModem>;
type EntityArrayResponseType = HttpResponse<IModem[]>;

@Injectable({ providedIn: 'root' })
export class ModemService {
  public resourceUrl = SERVER_API_URL + 'api/modems';

  constructor(protected http: HttpClient) {}

  create(modem: IModem): Observable<EntityResponseType> {
    return this.http.post<IModem>(this.resourceUrl, modem, { observe: 'response' });
  }

  update(modem: IModem): Observable<EntityResponseType> {
    return this.http.put<IModem>(this.resourceUrl, modem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IModem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IModem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
