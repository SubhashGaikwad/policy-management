import * as dayjs from 'dayjs';
import { IProductDetails } from 'app/entities/product-details/product-details.model';
import { ICompany } from 'app/entities/company/company.model';

export interface IProduct {
  id?: number;
  name?: string | null;
  planNo?: number | null;
  uinNo?: string | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  productDetails?: IProductDetails | null;
  company?: ICompany | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string | null,
    public planNo?: number | null,
    public uinNo?: string | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public productDetails?: IProductDetails | null,
    public company?: ICompany | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
