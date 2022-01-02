import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductDetails, getProductDetailsIdentifier } from '../product-details.model';

export type EntityResponseType = HttpResponse<IProductDetails>;
export type EntityArrayResponseType = HttpResponse<IProductDetails[]>;

@Injectable({ providedIn: 'root' })
export class ProductDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productDetails: IProductDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productDetails);
    return this.http
      .post<IProductDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(productDetails: IProductDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productDetails);
    return this.http
      .put<IProductDetails>(`${this.resourceUrl}/${getProductDetailsIdentifier(productDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(productDetails: IProductDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productDetails);
    return this.http
      .patch<IProductDetails>(`${this.resourceUrl}/${getProductDetailsIdentifier(productDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProductDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductDetailsToCollectionIfMissing(
    productDetailsCollection: IProductDetails[],
    ...productDetailsToCheck: (IProductDetails | null | undefined)[]
  ): IProductDetails[] {
    const productDetails: IProductDetails[] = productDetailsToCheck.filter(isPresent);
    if (productDetails.length > 0) {
      const productDetailsCollectionIdentifiers = productDetailsCollection.map(
        productDetailsItem => getProductDetailsIdentifier(productDetailsItem)!
      );
      const productDetailsToAdd = productDetails.filter(productDetailsItem => {
        const productDetailsIdentifier = getProductDetailsIdentifier(productDetailsItem);
        if (productDetailsIdentifier == null || productDetailsCollectionIdentifiers.includes(productDetailsIdentifier)) {
          return false;
        }
        productDetailsCollectionIdentifiers.push(productDetailsIdentifier);
        return true;
      });
      return [...productDetailsToAdd, ...productDetailsCollection];
    }
    return productDetailsCollection;
  }

  protected convertDateFromClient(productDetails: IProductDetails): IProductDetails {
    return Object.assign({}, productDetails, {
      activationDate: productDetails.activationDate?.isValid() ? productDetails.activationDate.toJSON() : undefined,
      lastModified: productDetails.lastModified?.isValid() ? productDetails.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.activationDate = res.body.activationDate ? dayjs(res.body.activationDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((productDetails: IProductDetails) => {
        productDetails.activationDate = productDetails.activationDate ? dayjs(productDetails.activationDate) : undefined;
        productDetails.lastModified = productDetails.lastModified ? dayjs(productDetails.lastModified) : undefined;
      });
    }
    return res;
  }
}
