import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductType, getProductTypeIdentifier } from '../product-type.model';

export type EntityResponseType = HttpResponse<IProductType>;
export type EntityArrayResponseType = HttpResponse<IProductType[]>;

@Injectable({ providedIn: 'root' })
export class ProductTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productType: IProductType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productType);
    return this.http
      .post<IProductType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(productType: IProductType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productType);
    return this.http
      .put<IProductType>(`${this.resourceUrl}/${getProductTypeIdentifier(productType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(productType: IProductType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productType);
    return this.http
      .patch<IProductType>(`${this.resourceUrl}/${getProductTypeIdentifier(productType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProductType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductTypeToCollectionIfMissing(
    productTypeCollection: IProductType[],
    ...productTypesToCheck: (IProductType | null | undefined)[]
  ): IProductType[] {
    const productTypes: IProductType[] = productTypesToCheck.filter(isPresent);
    if (productTypes.length > 0) {
      const productTypeCollectionIdentifiers = productTypeCollection.map(productTypeItem => getProductTypeIdentifier(productTypeItem)!);
      const productTypesToAdd = productTypes.filter(productTypeItem => {
        const productTypeIdentifier = getProductTypeIdentifier(productTypeItem);
        if (productTypeIdentifier == null || productTypeCollectionIdentifiers.includes(productTypeIdentifier)) {
          return false;
        }
        productTypeCollectionIdentifiers.push(productTypeIdentifier);
        return true;
      });
      return [...productTypesToAdd, ...productTypeCollection];
    }
    return productTypeCollection;
  }

  protected convertDateFromClient(productType: IProductType): IProductType {
    return Object.assign({}, productType, {
      lastModified: productType.lastModified?.isValid() ? productType.lastModified.toJSON() : undefined,
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
      res.body.forEach((productType: IProductType) => {
        productType.lastModified = productType.lastModified ? dayjs(productType.lastModified) : undefined;
      });
    }
    return res;
  }
}
