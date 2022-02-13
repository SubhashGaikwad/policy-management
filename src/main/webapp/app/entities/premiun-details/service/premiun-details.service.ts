import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPremiunDetails, getPremiunDetailsIdentifier } from '../premiun-details.model';

export type EntityResponseType = HttpResponse<IPremiunDetails>;
export type EntityArrayResponseType = HttpResponse<IPremiunDetails[]>;

@Injectable({ providedIn: 'root' })
export class PremiunDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/premiun-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(premiunDetails: IPremiunDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(premiunDetails);
    return this.http
      .post<IPremiunDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(premiunDetails: IPremiunDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(premiunDetails);
    return this.http
      .put<IPremiunDetails>(`${this.resourceUrl}/${getPremiunDetailsIdentifier(premiunDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(premiunDetails: IPremiunDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(premiunDetails);
    return this.http
      .patch<IPremiunDetails>(`${this.resourceUrl}/${getPremiunDetailsIdentifier(premiunDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPremiunDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPremiunDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPremiunDetailsToCollectionIfMissing(
    premiunDetailsCollection: IPremiunDetails[],
    ...premiunDetailsToCheck: (IPremiunDetails | null | undefined)[]
  ): IPremiunDetails[] {
    const premiunDetails: IPremiunDetails[] = premiunDetailsToCheck.filter(isPresent);
    if (premiunDetails.length > 0) {
      const premiunDetailsCollectionIdentifiers = premiunDetailsCollection.map(
        premiunDetailsItem => getPremiunDetailsIdentifier(premiunDetailsItem)!
      );
      const premiunDetailsToAdd = premiunDetails.filter(premiunDetailsItem => {
        const premiunDetailsIdentifier = getPremiunDetailsIdentifier(premiunDetailsItem);
        if (premiunDetailsIdentifier == null || premiunDetailsCollectionIdentifiers.includes(premiunDetailsIdentifier)) {
          return false;
        }
        premiunDetailsCollectionIdentifiers.push(premiunDetailsIdentifier);
        return true;
      });
      return [...premiunDetailsToAdd, ...premiunDetailsCollection];
    }
    return premiunDetailsCollection;
  }

  protected convertDateFromClient(premiunDetails: IPremiunDetails): IPremiunDetails {
    return Object.assign({}, premiunDetails, {
      lastModified: premiunDetails.lastModified?.isValid() ? premiunDetails.lastModified.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((premiunDetails: IPremiunDetails) => {
        premiunDetails.lastModified = premiunDetails.lastModified ? dayjs(premiunDetails.lastModified) : undefined;
      });
    }
    return res;
  }
}
