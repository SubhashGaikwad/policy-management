import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { PolicyStatus } from 'app/entities/enumerations/policy-status.model';
import { IPolicy, Policy } from '../policy.model';

import { PolicyService } from './policy.service';

describe('Policy Service', () => {
  let service: PolicyService;
  let httpMock: HttpTestingController;
  let elemDefault: IPolicy;
  let expectedResult: IPolicy | IPolicy[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PolicyService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      policyAmount: 0,
      instalmentAmount: 0,
      term: 0,
      instalmentPeriod: 0,
      instalmentDate: 0,
      status: PolicyStatus.OPEN,
      dateStart: currentDate,
      dateEnd: currentDate,
      maturityDate: currentDate,
      uinNo: 'AAAAAAA',
      lastModified: currentDate,
      lastModifiedBy: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateStart: currentDate.format(DATE_TIME_FORMAT),
          dateEnd: currentDate.format(DATE_TIME_FORMAT),
          maturityDate: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Policy', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateStart: currentDate.format(DATE_TIME_FORMAT),
          dateEnd: currentDate.format(DATE_TIME_FORMAT),
          maturityDate: currentDate.format(DATE_TIME_FORMAT),
          lastModified: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateStart: currentDate,
          dateEnd: currentDate,
          maturityDate: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.create(new Policy()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Policy', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          policyAmount: 1,
          instalmentAmount: 1,
          term: 1,
          instalmentPeriod: 1,
          instalmentDate: 1,
          status: 'BBBBBB',
          dateStart: currentDate.format(DATE_TIME_FORMAT),
          dateEnd: currentDate.format(DATE_TIME_FORMAT),
          maturityDate: currentDate.format(DATE_TIME_FORMAT),
          uinNo: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateStart: currentDate,
          dateEnd: currentDate,
          maturityDate: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Policy', () => {
      const patchObject = Object.assign(
        {
          policyAmount: 1,
          instalmentAmount: 1,
          status: 'BBBBBB',
          dateStart: currentDate.format(DATE_TIME_FORMAT),
          uinNo: 'BBBBBB',
          lastModifiedBy: 'BBBBBB',
        },
        new Policy()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateStart: currentDate,
          dateEnd: currentDate,
          maturityDate: currentDate,
          lastModified: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Policy', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          policyAmount: 1,
          instalmentAmount: 1,
          term: 1,
          instalmentPeriod: 1,
          instalmentDate: 1,
          status: 'BBBBBB',
          dateStart: currentDate.format(DATE_TIME_FORMAT),
          dateEnd: currentDate.format(DATE_TIME_FORMAT),
          maturityDate: currentDate.format(DATE_TIME_FORMAT),
          uinNo: 'BBBBBB',
          lastModified: currentDate.format(DATE_TIME_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateStart: currentDate,
          dateEnd: currentDate,
          maturityDate: currentDate,
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

    it('should delete a Policy', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPolicyToCollectionIfMissing', () => {
      it('should add a Policy to an empty array', () => {
        const policy: IPolicy = { id: 123 };
        expectedResult = service.addPolicyToCollectionIfMissing([], policy);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(policy);
      });

      it('should not add a Policy to an array that contains it', () => {
        const policy: IPolicy = { id: 123 };
        const policyCollection: IPolicy[] = [
          {
            ...policy,
          },
          { id: 456 },
        ];
        expectedResult = service.addPolicyToCollectionIfMissing(policyCollection, policy);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Policy to an array that doesn't contain it", () => {
        const policy: IPolicy = { id: 123 };
        const policyCollection: IPolicy[] = [{ id: 456 }];
        expectedResult = service.addPolicyToCollectionIfMissing(policyCollection, policy);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(policy);
      });

      it('should add only unique Policy to an array', () => {
        const policyArray: IPolicy[] = [{ id: 123 }, { id: 456 }, { id: 48137 }];
        const policyCollection: IPolicy[] = [{ id: 123 }];
        expectedResult = service.addPolicyToCollectionIfMissing(policyCollection, ...policyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const policy: IPolicy = { id: 123 };
        const policy2: IPolicy = { id: 456 };
        expectedResult = service.addPolicyToCollectionIfMissing([], policy, policy2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(policy);
        expect(expectedResult).toContain(policy2);
      });

      it('should accept null and undefined values', () => {
        const policy: IPolicy = { id: 123 };
        expectedResult = service.addPolicyToCollectionIfMissing([], null, policy, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(policy);
      });

      it('should return initial array if no Policy is added', () => {
        const policyCollection: IPolicy[] = [{ id: 123 }];
        expectedResult = service.addPolicyToCollectionIfMissing(policyCollection, undefined, null);
        expect(expectedResult).toEqual(policyCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
