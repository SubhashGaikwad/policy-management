import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicleDetails, getVehicleDetailsIdentifier } from '../vehicle-details.model';

export type EntityResponseType = HttpResponse<IVehicleDetails>;
export type EntityArrayResponseType = HttpResponse<IVehicleDetails[]>;

@Injectable({ providedIn: 'root' })
export class VehicleDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicle-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vehicleDetails: IVehicleDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleDetails);
    return this.http
      .post<IVehicleDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vehicleDetails: IVehicleDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleDetails);
    return this.http
      .put<IVehicleDetails>(`${this.resourceUrl}/${getVehicleDetailsIdentifier(vehicleDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vehicleDetails: IVehicleDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleDetails);
    return this.http
      .patch<IVehicleDetails>(`${this.resourceUrl}/${getVehicleDetailsIdentifier(vehicleDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVehicleDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVehicleDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVehicleDetailsToCollectionIfMissing(
    vehicleDetailsCollection: IVehicleDetails[],
    ...vehicleDetailsToCheck: (IVehicleDetails | null | undefined)[]
  ): IVehicleDetails[] {
    const vehicleDetails: IVehicleDetails[] = vehicleDetailsToCheck.filter(isPresent);
    if (vehicleDetails.length > 0) {
      const vehicleDetailsCollectionIdentifiers = vehicleDetailsCollection.map(
        vehicleDetailsItem => getVehicleDetailsIdentifier(vehicleDetailsItem)!
      );
      const vehicleDetailsToAdd = vehicleDetails.filter(vehicleDetailsItem => {
        const vehicleDetailsIdentifier = getVehicleDetailsIdentifier(vehicleDetailsItem);
        if (vehicleDetailsIdentifier == null || vehicleDetailsCollectionIdentifiers.includes(vehicleDetailsIdentifier)) {
          return false;
        }
        vehicleDetailsCollectionIdentifiers.push(vehicleDetailsIdentifier);
        return true;
      });
      return [...vehicleDetailsToAdd, ...vehicleDetailsCollection];
    }
    return vehicleDetailsCollection;
  }

  protected convertDateFromClient(vehicleDetails: IVehicleDetails): IVehicleDetails {
    return Object.assign({}, vehicleDetails, {
      registrationDate: vehicleDetails.registrationDate?.isValid() ? vehicleDetails.registrationDate.toJSON() : undefined,
      lastModified: vehicleDetails.lastModified?.isValid() ? vehicleDetails.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.registrationDate = res.body.registrationDate ? dayjs(res.body.registrationDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vehicleDetails: IVehicleDetails) => {
        vehicleDetails.registrationDate = vehicleDetails.registrationDate ? dayjs(vehicleDetails.registrationDate) : undefined;
        vehicleDetails.lastModified = vehicleDetails.lastModified ? dayjs(vehicleDetails.lastModified) : undefined;
      });
    }
    return res;
  }
}
