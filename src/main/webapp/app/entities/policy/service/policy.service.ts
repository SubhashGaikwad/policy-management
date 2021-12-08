import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPolicy, getPolicyIdentifier } from '../policy.model';

export type EntityResponseType = HttpResponse<IPolicy>;
export type EntityArrayResponseType = HttpResponse<IPolicy[]>;

@Injectable({ providedIn: 'root' })
export class PolicyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/policies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(policy: IPolicy): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(policy);
    return this.http
      .post<IPolicy>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(policy: IPolicy): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(policy);
    return this.http
      .put<IPolicy>(`${this.resourceUrl}/${getPolicyIdentifier(policy) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(policy: IPolicy): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(policy);
    return this.http
      .patch<IPolicy>(`${this.resourceUrl}/${getPolicyIdentifier(policy) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPolicy>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPolicy[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPolicyToCollectionIfMissing(policyCollection: IPolicy[], ...policiesToCheck: (IPolicy | null | undefined)[]): IPolicy[] {
    const policies: IPolicy[] = policiesToCheck.filter(isPresent);
    if (policies.length > 0) {
      const policyCollectionIdentifiers = policyCollection.map(policyItem => getPolicyIdentifier(policyItem)!);
      const policiesToAdd = policies.filter(policyItem => {
        const policyIdentifier = getPolicyIdentifier(policyItem);
        if (policyIdentifier == null || policyCollectionIdentifiers.includes(policyIdentifier)) {
          return false;
        }
        policyCollectionIdentifiers.push(policyIdentifier);
        return true;
      });
      return [...policiesToAdd, ...policyCollection];
    }
    return policyCollection;
  }

  protected convertDateFromClient(policy: IPolicy): IPolicy {
    return Object.assign({}, policy, {
      dateStart: policy.dateStart?.isValid() ? policy.dateStart.toJSON() : undefined,
      dateEnd: policy.dateEnd?.isValid() ? policy.dateEnd.toJSON() : undefined,
      maturityDate: policy.maturityDate?.isValid() ? policy.maturityDate.toJSON() : undefined,
      lastModified: policy.lastModified?.isValid() ? policy.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateStart = res.body.dateStart ? dayjs(res.body.dateStart) : undefined;
      res.body.dateEnd = res.body.dateEnd ? dayjs(res.body.dateEnd) : undefined;
      res.body.maturityDate = res.body.maturityDate ? dayjs(res.body.maturityDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((policy: IPolicy) => {
        policy.dateStart = policy.dateStart ? dayjs(policy.dateStart) : undefined;
        policy.dateEnd = policy.dateEnd ? dayjs(policy.dateEnd) : undefined;
        policy.maturityDate = policy.maturityDate ? dayjs(policy.maturityDate) : undefined;
        policy.lastModified = policy.lastModified ? dayjs(policy.lastModified) : undefined;
      });
    }
    return res;
  }
}
