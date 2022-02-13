import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBankDetails, getBankDetailsIdentifier } from '../bank-details.model';

export type EntityResponseType = HttpResponse<IBankDetails>;
export type EntityArrayResponseType = HttpResponse<IBankDetails[]>;

@Injectable({ providedIn: 'root' })
export class BankDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bank-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bankDetails: IBankDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankDetails);
    return this.http
      .post<IBankDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bankDetails: IBankDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankDetails);
    return this.http
      .put<IBankDetails>(`${this.resourceUrl}/${getBankDetailsIdentifier(bankDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(bankDetails: IBankDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankDetails);
    return this.http
      .patch<IBankDetails>(`${this.resourceUrl}/${getBankDetailsIdentifier(bankDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBankDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBankDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBankDetailsToCollectionIfMissing(
    bankDetailsCollection: IBankDetails[],
    ...bankDetailsToCheck: (IBankDetails | null | undefined)[]
  ): IBankDetails[] {
    const bankDetails: IBankDetails[] = bankDetailsToCheck.filter(isPresent);
    if (bankDetails.length > 0) {
      const bankDetailsCollectionIdentifiers = bankDetailsCollection.map(bankDetailsItem => getBankDetailsIdentifier(bankDetailsItem)!);
      const bankDetailsToAdd = bankDetails.filter(bankDetailsItem => {
        const bankDetailsIdentifier = getBankDetailsIdentifier(bankDetailsItem);
        if (bankDetailsIdentifier == null || bankDetailsCollectionIdentifiers.includes(bankDetailsIdentifier)) {
          return false;
        }
        bankDetailsCollectionIdentifiers.push(bankDetailsIdentifier);
        return true;
      });
      return [...bankDetailsToAdd, ...bankDetailsCollection];
    }
    return bankDetailsCollection;
  }

  protected convertDateFromClient(bankDetails: IBankDetails): IBankDetails {
    return Object.assign({}, bankDetails, {
      lastModified: bankDetails.lastModified?.isValid() ? bankDetails.lastModified.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((bankDetails: IBankDetails) => {
        bankDetails.lastModified = bankDetails.lastModified ? dayjs(bankDetails.lastModified) : undefined;
      });
    }
    return res;
  }
}
