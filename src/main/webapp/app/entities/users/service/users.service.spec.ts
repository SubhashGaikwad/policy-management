import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { StatusInd } from 'app/entities/enumerations/status-ind.model';
import { IUsers, Users } from '../users.model';

import { UsersService } from './users.service';

describe('Users Service', () => {
  let service: UsersService;
  let httpMock: HttpTestingController;
  let elemDefault: IUsers;
  let expectedResult: IUsers | IUsers[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UsersService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      firstName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      birthDate: currentDate,
      userTypeId: 0,
      username: 'AAAAAAA',
      password: 'AAAAAAA',
      email: 'AAAAAAA',
      imageUrl: 'AAAAAAA',
      status: StatusInd.A,
      activated: false,
      mobileNo: 'AAAAAAA',
      oneTimePassword: 'AAAAAAA',
      otpExpiryTime: currentDate,
      lastModified: currentDate,
      lastModifiedBy: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          birthDate: currentDate.format(DATE_TIME_FORMAT),
          otpExpiryTime: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Users', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          birthDate: currentDate.format(DATE_TIME_FORMAT),
          otpExpiryTime: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          birthDate: currentDate,
          otpExpiryTime: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new Users()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Users', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          birthDate: currentDate.format(DATE_TIME_FORMAT),
          userTypeId: 1,
          username: 'BBBBBB',
          password: 'BBBBBB',
          email: 'BBBBBB',
          imageUrl: 'BBBBBB',
          status: 'BBBBBB',
          activated: true,
          mobileNo: 'BBBBBB',
          oneTimePassword: 'BBBBBB',
          otpExpiryTime: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          birthDate: currentDate,
          otpExpiryTime: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Users', () => {
      const patchObject = Object.assign(
        {
          lastName: 'BBBBBB',
          birthDate: currentDate.format(DATE_TIME_FORMAT),
          email: 'BBBBBB',
          activated: true,
        },
        new Users()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          birthDate: currentDate,
          otpExpiryTime: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Users', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          lastName: 'BBBBBB',
          birthDate: currentDate.format(DATE_TIME_FORMAT),
          userTypeId: 1,
          username: 'BBBBBB',
          password: 'BBBBBB',
          email: 'BBBBBB',
          imageUrl: 'BBBBBB',
          status: 'BBBBBB',
          activated: true,
          mobileNo: 'BBBBBB',
          oneTimePassword: 'BBBBBB',
          otpExpiryTime: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          birthDate: currentDate,
          otpExpiryTime: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Users', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUsersToCollectionIfMissing', () => {
      it('should add a Users to an empty array', () => {
        const users: IUsers = { id: 123 };
        expectedResult = service.addUsersToCollectionIfMissing([], users);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(users);
      });

      it('should not add a Users to an array that contains it', () => {
        const users: IUsers = { id: 123 };
        const usersCollection: IUsers[] = [
          {
            ...users,
          },
          { id: 456 },
        ];
        expectedResult = service.addUsersToCollectionIfMissing(usersCollection, users);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Users to an array that doesn't contain it", () => {
        const users: IUsers = { id: 123 };
        const usersCollection: IUsers[] = [{ id: 456 }];
        expectedResult = service.addUsersToCollectionIfMissing(usersCollection, users);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(users);
      });

      it('should add only unique Users to an array', () => {
        const usersArray: IUsers[] = [{ id: 123 }, { id: 456 }, { id: 47036 }];
        const usersCollection: IUsers[] = [{ id: 123 }];
        expectedResult = service.addUsersToCollectionIfMissing(usersCollection, ...usersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const users: IUsers = { id: 123 };
        const users2: IUsers = { id: 456 };
        expectedResult = service.addUsersToCollectionIfMissing([], users, users2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(users);
        expect(expectedResult).toContain(users2);
      });

      it('should accept null and undefined values', () => {
        const users: IUsers = { id: 123 };
        expectedResult = service.addUsersToCollectionIfMissing([], null, users, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(users);
      });

      it('should return initial array if no Users is added', () => {
        const usersCollection: IUsers[] = [{ id: 123 }];
        expectedResult = service.addUsersToCollectionIfMissing(usersCollection, undefined, null);
        expect(expectedResult).toEqual(usersCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
