import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUsersType, getUsersTypeIdentifier } from '../users-type.model';

export type EntityResponseType = HttpResponse<IUsersType>;
export type EntityArrayResponseType = HttpResponse<IUsersType[]>;

@Injectable({ providedIn: 'root' })
export class UsersTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/users-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(usersType: IUsersType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usersType);
    return this.http
      .post<IUsersType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(usersType: IUsersType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usersType);
    return this.http
      .put<IUsersType>(`${this.resourceUrl}/${getUsersTypeIdentifier(usersType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(usersType: IUsersType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usersType);
    return this.http
      .patch<IUsersType>(`${this.resourceUrl}/${getUsersTypeIdentifier(usersType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUsersType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUsersType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUsersTypeToCollectionIfMissing(
    usersTypeCollection: IUsersType[],
    ...usersTypesToCheck: (IUsersType | null | undefined)[]
  ): IUsersType[] {
    const usersTypes: IUsersType[] = usersTypesToCheck.filter(isPresent);
    if (usersTypes.length > 0) {
      const usersTypeCollectionIdentifiers = usersTypeCollection.map(usersTypeItem => getUsersTypeIdentifier(usersTypeItem)!);
      const usersTypesToAdd = usersTypes.filter(usersTypeItem => {
        const usersTypeIdentifier = getUsersTypeIdentifier(usersTypeItem);
        if (usersTypeIdentifier == null || usersTypeCollectionIdentifiers.includes(usersTypeIdentifier)) {
          return false;
        }
        usersTypeCollectionIdentifiers.push(usersTypeIdentifier);
        return true;
      });
      return [...usersTypesToAdd, ...usersTypeCollection];
    }
    return usersTypeCollection;
  }

  protected convertDateFromClient(usersType: IUsersType): IUsersType {
    return Object.assign({}, usersType, {
      lastModified: usersType.lastModified?.isValid() ? usersType.lastModified.toJSON() : undefined,
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
      res.body.forEach((usersType: IUsersType) => {
        usersType.lastModified = usersType.lastModified ? dayjs(usersType.lastModified) : undefined;
      });
    }
    return res;
  }
}
