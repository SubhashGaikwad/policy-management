import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRider, getRiderIdentifier } from '../rider.model';

export type EntityResponseType = HttpResponse<IRider>;
export type EntityArrayResponseType = HttpResponse<IRider[]>;

@Injectable({ providedIn: 'root' })
export class RiderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/riders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rider: IRider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rider);
    return this.http
      .post<IRider>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rider: IRider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rider);
    return this.http
      .put<IRider>(`${this.resourceUrl}/${getRiderIdentifier(rider) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rider: IRider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rider);
    return this.http
      .patch<IRider>(`${this.resourceUrl}/${getRiderIdentifier(rider) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRider>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRider[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRiderToCollectionIfMissing(riderCollection: IRider[], ...ridersToCheck: (IRider | null | undefined)[]): IRider[] {
    const riders: IRider[] = ridersToCheck.filter(isPresent);
    if (riders.length > 0) {
      const riderCollectionIdentifiers = riderCollection.map(riderItem => getRiderIdentifier(riderItem)!);
      const ridersToAdd = riders.filter(riderItem => {
        const riderIdentifier = getRiderIdentifier(riderItem);
        if (riderIdentifier == null || riderCollectionIdentifiers.includes(riderIdentifier)) {
          return false;
        }
        riderCollectionIdentifiers.push(riderIdentifier);
        return true;
      });
      return [...ridersToAdd, ...riderCollection];
    }
    return riderCollection;
  }

  protected convertDateFromClient(rider: IRider): IRider {
    return Object.assign({}, rider, {
      commDate: rider.commDate?.isValid() ? rider.commDate.toJSON() : undefined,
      lastModified: rider.lastModified?.isValid() ? rider.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.commDate = res.body.commDate ? dayjs(res.body.commDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rider: IRider) => {
        rider.commDate = rider.commDate ? dayjs(rider.commDate) : undefined;
        rider.lastModified = rider.lastModified ? dayjs(rider.lastModified) : undefined;
      });
    }
    return res;
  }
}
