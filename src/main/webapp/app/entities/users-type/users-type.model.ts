import * as dayjs from 'dayjs';
import { IUsers } from 'app/entities/users/users.model';

export interface IUsersType {
  id?: number;
  name?: string | null;
  lastModified?: dayjs.Dayjs;
  lastModifiedBy?: string;
  users?: IUsers | null;
}

export class UsersType implements IUsersType {
  constructor(
    public id?: number,
    public name?: string | null,
    public lastModified?: dayjs.Dayjs,
    public lastModifiedBy?: string,
    public users?: IUsers | null
  ) {}
}

export function getUsersTypeIdentifier(usersType: IUsersType): number | undefined {
  return usersType.id;
}
