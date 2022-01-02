import dayjs from 'dayjs/esm';
import { IPolicy } from 'app/entities/policy/policy.model';

export interface INominee {
  id?: number;
  name?: number | null;
  relation?: string | null;
  nomineePercentage?: number | null;
  contactNo?: number | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  policy?: IPolicy | null;
}

export class Nominee implements INominee {
  constructor(
    public id?: number,
    public name?: number | null,
    public relation?: string | null,
    public nomineePercentage?: number | null,
    public contactNo?: number | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public policy?: IPolicy | null
  ) {}
}

export function getNomineeIdentifier(nominee: INominee): number | undefined {
  return nominee.id;
}
