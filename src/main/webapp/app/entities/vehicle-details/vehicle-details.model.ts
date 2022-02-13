import dayjs from 'dayjs/esm';
import { IParameterLookup } from 'app/entities/parameter-lookup/parameter-lookup.model';
import { Zone } from 'app/entities/enumerations/zone.model';

export interface IVehicleDetails {
  id?: number;
  name?: number | null;
  invoiceValue?: string | null;
  idv?: string | null;
  enginNumber?: string | null;
  chassisNumber?: string | null;
  registrationNumber?: string | null;
  seatingCapacity?: number | null;
  zone?: Zone | null;
  yearOfManufacturing?: string | null;
  registrationDate?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  parameterLookups?: IParameterLookup[] | null;
}

export class VehicleDetails implements IVehicleDetails {
  constructor(
    public id?: number,
    public name?: number | null,
    public invoiceValue?: string | null,
    public idv?: string | null,
    public enginNumber?: string | null,
    public chassisNumber?: string | null,
    public registrationNumber?: string | null,
    public seatingCapacity?: number | null,
    public zone?: Zone | null,
    public yearOfManufacturing?: string | null,
    public registrationDate?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public parameterLookups?: IParameterLookup[] | null
  ) {}
}

export function getVehicleDetailsIdentifier(vehicleDetails: IVehicleDetails): number | undefined {
  return vehicleDetails.id;
}
