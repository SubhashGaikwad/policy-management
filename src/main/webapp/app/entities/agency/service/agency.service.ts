import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgency, getAgencyIdentifier } from '../agency.model';

export type EntityResponseType = HttpResponse<IAgency>;
export type EntityArrayResponseType = HttpResponse<IAgency[]>;

@Injectable({ providedIn: 'root' })
export class AgencyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agencies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(agency: IAgency): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agency);
    return this.http
      .post<IAgency>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(agency: IAgency): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agency);
    return this.http
      .put<IAgency>(`${this.resourceUrl}/${getAgencyIdentifier(agency) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(agency: IAgency): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agency);
    return this.http
      .patch<IAgency>(`${this.resourceUrl}/${getAgencyIdentifier(agency) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAgency>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgency[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAgencyToCollectionIfMissing(agencyCollection: IAgency[], ...agenciesToCheck: (IAgency | null | undefined)[]): IAgency[] {
    const agencies: IAgency[] = agenciesToCheck.filter(isPresent);
    if (agencies.length > 0) {
      const agencyCollectionIdentifiers = agencyCollection.map(agencyItem => getAgencyIdentifier(agencyItem)!);
      const agenciesToAdd = agencies.filter(agencyItem => {
        const agencyIdentifier = getAgencyIdentifier(agencyItem);
        if (agencyIdentifier == null || agencyCollectionIdentifiers.includes(agencyIdentifier)) {
          return false;
        }
        agencyCollectionIdentifiers.push(agencyIdentifier);
        return true;
      });
      return [...agenciesToAdd, ...agencyCollection];
    }
    return agencyCollection;
  }

  protected convertDateFromClient(agency: IAgency): IAgency {
    return Object.assign({}, agency, {
      lastModified: agency.lastModified?.isValid() ? agency.lastModified.toJSON() : undefined,
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
      res.body.forEach((agency: IAgency) => {
        agency.lastModified = agency.lastModified ? dayjs(agency.lastModified) : undefined;
      });
    }
    return res;
  }
}
