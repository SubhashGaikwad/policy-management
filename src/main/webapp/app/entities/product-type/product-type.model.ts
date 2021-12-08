import * as dayjs from 'dayjs';
import { IProductDetails } from 'app/entities/product-details/product-details.model';

export interface IProductType {
  id?: number;
  name?: string | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  productDetails?: IProductDetails | null;
}

export class ProductType implements IProductType {
  constructor(
    public id?: number,
    public name?: string | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public productDetails?: IProductDetails | null
  ) {}
}

export function getProductTypeIdentifier(productType: IProductType): number | undefined {
  return productType.id;
}
