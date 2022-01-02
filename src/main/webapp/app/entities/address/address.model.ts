import dayjs from 'dayjs/esm';
import { IUsers } from 'app/entities/users/users.model';
import { ICompany } from 'app/entities/company/company.model';

export interface IAddress {
  id?: number;
  area?: string | null;
  landmark?: string | null;
  taluka?: string | null;
  district?: string | null;
  state?: string | null;
  pincode?: number | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  users?: IUsers | null;
  company?: ICompany | null;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public area?: string | null,
    public landmark?: string | null,
    public taluka?: string | null,
    public district?: string | null,
    public state?: string | null,
    public pincode?: number | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public users?: IUsers | null,
    public company?: ICompany | null
  ) {}
}

export function getAddressIdentifier(address: IAddress): number | undefined {
  return address.id;
}
