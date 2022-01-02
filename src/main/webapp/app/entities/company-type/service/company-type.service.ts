import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompanyType, getCompanyTypeIdentifier } from '../company-type.model';

export type EntityResponseType = HttpResponse<ICompanyType>;
export type EntityArrayResponseType = HttpResponse<ICompanyType[]>;

@Injectable({ providedIn: 'root' })
export class CompanyTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/company-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(companyType: ICompanyType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyType);
    return this.http
      .post<ICompanyType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(companyType: ICompanyType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyType);
    return this.http
      .put<ICompanyType>(`${this.resourceUrl}/${getCompanyTypeIdentifier(companyType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(companyType: ICompanyType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(companyType);
    return this.http
      .patch<ICompanyType>(`${this.resourceUrl}/${getCompanyTypeIdentifier(companyType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICompanyType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICompanyType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompanyTypeToCollectionIfMissing(
    companyTypeCollection: ICompanyType[],
    ...companyTypesToCheck: (ICompanyType | null | undefined)[]
  ): ICompanyType[] {
    const companyTypes: ICompanyType[] = companyTypesToCheck.filter(isPresent);
    if (companyTypes.length > 0) {
      const companyTypeCollectionIdentifiers = companyTypeCollection.map(companyTypeItem => getCompanyTypeIdentifier(companyTypeItem)!);
      const companyTypesToAdd = companyTypes.filter(companyTypeItem => {
        const companyTypeIdentifier = getCompanyTypeIdentifier(companyTypeItem);
        if (companyTypeIdentifier == null || companyTypeCollectionIdentifiers.includes(companyTypeIdentifier)) {
          return false;
        }
        companyTypeCollectionIdentifiers.push(companyTypeIdentifier);
        return true;
      });
      return [...companyTypesToAdd, ...companyTypeCollection];
    }
    return companyTypeCollection;
  }

  protected convertDateFromClient(companyType: ICompanyType): ICompanyType {
    return Object.assign({}, companyType, {
      lastModified: companyType.lastModified?.isValid() ? companyType.lastModified.toJSON() : undefined,
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
      res.body.forEach((companyType: ICompanyType) => {
        companyType.lastModified = companyType.lastModified ? dayjs(companyType.lastModified) : undefined;
      });
    }
    return res;
  }
}
