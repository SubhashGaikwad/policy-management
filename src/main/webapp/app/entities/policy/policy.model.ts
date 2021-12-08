import * as dayjs from 'dayjs';
import { INominee } from 'app/entities/nominee/nominee.model';
import { IUsers } from 'app/entities/users/users.model';
import { PolicyStatus } from 'app/entities/enumerations/policy-status.model';

export interface IPolicy {
  id?: number;
  policyAmount?: number | null;
  instalmentAmount?: number | null;
  term?: number | null;
  instalmentPeriod?: number | null;
  instalmentDate?: number | null;
  status?: PolicyStatus | null;
  dateStart?: dayjs.Dayjs;
  dateEnd?: dayjs.Dayjs;
  maturityDate?: dayjs.Dayjs;
  uinNo?: string | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  nominees?: INominee[] | null;
  users?: IUsers | null;
}

export class Policy implements IPolicy {
  constructor(
    public id?: number,
    public policyAmount?: number | null,
    public instalmentAmount?: number | null,
    public term?: number | null,
    public instalmentPeriod?: number | null,
    public instalmentDate?: number | null,
    public status?: PolicyStatus | null,
    public dateStart?: dayjs.Dayjs,
    public dateEnd?: dayjs.Dayjs,
    public maturityDate?: dayjs.Dayjs,
    public uinNo?: string | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public nominees?: INominee[] | null,
    public users?: IUsers | null
  ) {}
}

export function getPolicyIdentifier(policy: IPolicy): number | undefined {
  return policy.id;
}
