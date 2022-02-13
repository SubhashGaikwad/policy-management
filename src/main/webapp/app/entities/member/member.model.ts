import dayjs from 'dayjs/esm';
import { IPolicy } from 'app/entities/policy/policy.model';

export interface IMember {
  id?: number;
  name?: number | null;
  age?: number | null;
  relation?: string | null;
  contactNo?: number | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  policy?: IPolicy | null;
}

export class Member implements IMember {
  constructor(
    public id?: number,
    public name?: number | null,
    public age?: number | null,
    public relation?: string | null,
    public contactNo?: number | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public policy?: IPolicy | null
  ) {}
}

export function getMemberIdentifier(member: IMember): number | undefined {
  return member.id;
}
