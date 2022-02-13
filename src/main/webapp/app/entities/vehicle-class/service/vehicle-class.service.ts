import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicleClass, getVehicleClassIdentifier } from '../vehicle-class.model';

export type EntityResponseType = HttpResponse<IVehicleClass>;
export type EntityArrayResponseType = HttpResponse<IVehicleClass[]>;

@Injectable({ providedIn: 'root' })
export class VehicleClassService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicle-classes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vehicleClass: IVehicleClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleClass);
    return this.http
      .post<IVehicleClass>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vehicleClass: IVehicleClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleClass);
    return this.http
      .put<IVehicleClass>(`${this.resourceUrl}/${getVehicleClassIdentifier(vehicleClass) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vehicleClass: IVehicleClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleClass);
    return this.http
      .patch<IVehicleClass>(`${this.resourceUrl}/${getVehicleClassIdentifier(vehicleClass) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVehicleClass>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVehicleClass[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVehicleClassToCollectionIfMissing(
    vehicleClassCollection: IVehicleClass[],
    ...vehicleClassesToCheck: (IVehicleClass | null | undefined)[]
  ): IVehicleClass[] {
    const vehicleClasses: IVehicleClass[] = vehicleClassesToCheck.filter(isPresent);
    if (vehicleClasses.length > 0) {
      const vehicleClassCollectionIdentifiers = vehicleClassCollection.map(
        vehicleClassItem => getVehicleClassIdentifier(vehicleClassItem)!
      );
      const vehicleClassesToAdd = vehicleClasses.filter(vehicleClassItem => {
        const vehicleClassIdentifier = getVehicleClassIdentifier(vehicleClassItem);
        if (vehicleClassIdentifier == null || vehicleClassCollectionIdentifiers.includes(vehicleClassIdentifier)) {
          return false;
        }
        vehicleClassCollectionIdentifiers.push(vehicleClassIdentifier);
        return true;
      });
      return [...vehicleClassesToAdd, ...vehicleClassCollection];
    }
    return vehicleClassCollection;
  }

  protected convertDateFromClient(vehicleClass: IVehicleClass): IVehicleClass {
    return Object.assign({}, vehicleClass, {
      lastModified: vehicleClass.lastModified?.isValid() ? vehicleClass.lastModified.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((vehicleClass: IVehicleClass) => {
        vehicleClass.lastModified = vehicleClass.lastModified ? dayjs(vehicleClass.lastModified) : undefined;
      });
    }
    return res;
  }
}
