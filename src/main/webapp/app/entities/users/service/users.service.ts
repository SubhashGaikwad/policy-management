import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUsers, getUsersIdentifier } from '../users.model';

export type EntityResponseType = HttpResponse<IUsers>;
export type EntityArrayResponseType = HttpResponse<IUsers[]>;

@Injectable({ providedIn: 'root' })
export class UsersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(users: IUsers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(users);
    return this.http
      .post<IUsers>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(users: IUsers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(users);
    return this.http
      .put<IUsers>(`${this.resourceUrl}/${getUsersIdentifier(users) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(users: IUsers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(users);
    return this.http
      .patch<IUsers>(`${this.resourceUrl}/${getUsersIdentifier(users) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUsers>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUsers[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUsersToCollectionIfMissing(usersCollection: IUsers[], ...usersToCheck: (IUsers | null | undefined)[]): IUsers[] {
    const users: IUsers[] = usersToCheck.filter(isPresent);
    if (users.length > 0) {
      const usersCollectionIdentifiers = usersCollection.map(usersItem => getUsersIdentifier(usersItem)!);
      const usersToAdd = users.filter(usersItem => {
        const usersIdentifier = getUsersIdentifier(usersItem);
        if (usersIdentifier == null || usersCollectionIdentifiers.includes(usersIdentifier)) {
          return false;
        }
        usersCollectionIdentifiers.push(usersIdentifier);
        return true;
      });
      return [...usersToAdd, ...usersCollection];
    }
    return usersCollection;
  }

  protected convertDateFromClient(users: IUsers): IUsers {
    return Object.assign({}, users, {
      birthDate: users.birthDate?.isValid() ? users.birthDate.toJSON() : undefined,
      marriageDate: users.marriageDate?.isValid() ? users.marriageDate.toJSON() : undefined,
      licenceExpiryDate: users.licenceExpiryDate?.isValid() ? users.licenceExpiryDate.toJSON() : undefined,
      otpExpiryTime: users.otpExpiryTime?.isValid() ? users.otpExpiryTime.toJSON() : undefined,
      lastModified: users.lastModified?.isValid() ? users.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.birthDate = res.body.birthDate ? dayjs(res.body.birthDate) : undefined;
      res.body.marriageDate = res.body.marriageDate ? dayjs(res.body.marriageDate) : undefined;
      res.body.licenceExpiryDate = res.body.licenceExpiryDate ? dayjs(res.body.licenceExpiryDate) : undefined;
      res.body.otpExpiryTime = res.body.otpExpiryTime ? dayjs(res.body.otpExpiryTime) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((users: IUsers) => {
        users.birthDate = users.birthDate ? dayjs(users.birthDate) : undefined;
        users.marriageDate = users.marriageDate ? dayjs(users.marriageDate) : undefined;
        users.licenceExpiryDate = users.licenceExpiryDate ? dayjs(users.licenceExpiryDate) : undefined;
        users.otpExpiryTime = users.otpExpiryTime ? dayjs(users.otpExpiryTime) : undefined;
        users.lastModified = users.lastModified ? dayjs(users.lastModified) : undefined;
      });
    }
    return res;
  }
}
