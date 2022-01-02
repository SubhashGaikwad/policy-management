import * as dayjs from 'dayjs';
import { IPolicy } from 'app/entities/policy/policy.model';

export interface IVehicleClass {
  id?: number;
  name?: number | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  policy?: IPolicy | null;
}

export class VehicleClass implements IVehicleClass {
  constructor(
    public id?: number,
    public name?: number | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public policy?: IPolicy | null
  ) {}
}

export function getVehicleClassIdentifier(vehicleClass: IVehicleClass): number | undefined {
  return vehicleClass.id;
}
