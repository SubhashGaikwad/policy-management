import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { PremiumMode } from 'app/entities/enumerations/premium-mode.model';
import { PolicyStatus } from 'app/entities/enumerations/policy-status.model';
import { Zone } from 'app/entities/enumerations/zone.model';
import { PolicyType } from 'app/entities/enumerations/policy-type.model';
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
      policyNumber: 'AAAAAAA',
      term: 0,
      ppt: 0,
      commDate: currentDate,
      proposerName: 'AAAAAAA',
      sumAssuredAmount: 0,
      premiumMode: PremiumMode.YEARLY,
      basicPremium: 0,
      extraPremium: 0,
      gst: 'AAAAAAA',
      status: PolicyStatus.OPEN,
      totalPremiun: 'AAAAAAA',
      gstFirstYear: 'AAAAAAA',
      netPremium: 'AAAAAAA',
      taxBeneficiary: 'AAAAAAA',
      policyReceived: false,
      previousPolicy: 0,
      policyStartDate: currentDate,
      policyEndDate: currentDate,
      period: 'AAAAAAA',
      claimDone: false,
      freeHeathCheckup: false,
      zone: Zone.A,
      noOfYear: 0,
      floaterSum: 'AAAAAAA',
      tpa: 'AAAAAAA',
      paymentDate: currentDate,
      policyType: PolicyType.LIFE,
      paToOwner: 'AAAAAAA',
      paToOther: 'AAAAAAA',
      loading: 0,
      riskCoveredFrom: currentDate,
      riskCoveredTo: currentDate,
      notes: 'AAAAAAA',
      freeField1: 'AAAAAAA',
      freeField2: 'AAAAAAA',
      freeField3: 'AAAAAAA',
      freeField4: 'AAAAAAA',
      freeField5: 'AAAAAAA',
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
          commDate: currentDate.format(DATE_FORMAT),
          policyStartDate: currentDate.format(DATE_FORMAT),
          policyEndDate: currentDate.format(DATE_FORMAT),
          paymentDate: currentDate.format(DATE_FORMAT),
          riskCoveredFrom: currentDate.format(DATE_FORMAT),
          riskCoveredTo: currentDate.format(DATE_FORMAT),
          maturityDate: currentDate.format(DATE_FORMAT),
          lastModified: currentDate.format(DATE_FORMAT),
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
          commDate: currentDate.format(DATE_FORMAT),
          policyStartDate: currentDate.format(DATE_FORMAT),
          policyEndDate: currentDate.format(DATE_FORMAT),
          paymentDate: currentDate.format(DATE_FORMAT),
          riskCoveredFrom: currentDate.format(DATE_FORMAT),
          riskCoveredTo: currentDate.format(DATE_FORMAT),
          maturityDate: currentDate.format(DATE_FORMAT),
          lastModified: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commDate: currentDate,
          policyStartDate: currentDate,
          policyEndDate: currentDate,
          paymentDate: currentDate,
          riskCoveredFrom: currentDate,
          riskCoveredTo: currentDate,
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
          policyNumber: 'BBBBBB',
          term: 1,
          ppt: 1,
          commDate: currentDate.format(DATE_FORMAT),
          proposerName: 'BBBBBB',
          sumAssuredAmount: 1,
          premiumMode: 'BBBBBB',
          basicPremium: 1,
          extraPremium: 1,
          gst: 'BBBBBB',
          status: 'BBBBBB',
          totalPremiun: 'BBBBBB',
          gstFirstYear: 'BBBBBB',
          netPremium: 'BBBBBB',
          taxBeneficiary: 'BBBBBB',
          policyReceived: true,
          previousPolicy: 1,
          policyStartDate: currentDate.format(DATE_FORMAT),
          policyEndDate: currentDate.format(DATE_FORMAT),
          period: 'BBBBBB',
          claimDone: true,
          freeHeathCheckup: true,
          zone: 'BBBBBB',
          noOfYear: 1,
          floaterSum: 'BBBBBB',
          tpa: 'BBBBBB',
          paymentDate: currentDate.format(DATE_FORMAT),
          policyType: 'BBBBBB',
          paToOwner: 'BBBBBB',
          paToOther: 'BBBBBB',
          loading: 1,
          riskCoveredFrom: currentDate.format(DATE_FORMAT),
          riskCoveredTo: currentDate.format(DATE_FORMAT),
          notes: 'BBBBBB',
          freeField1: 'BBBBBB',
          freeField2: 'BBBBBB',
          freeField3: 'BBBBBB',
          freeField4: 'BBBBBB',
          freeField5: 'BBBBBB',
          maturityDate: currentDate.format(DATE_FORMAT),
          uinNo: 'BBBBBB',
          lastModified: currentDate.format(DATE_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commDate: currentDate,
          policyStartDate: currentDate,
          policyEndDate: currentDate,
          paymentDate: currentDate,
          riskCoveredFrom: currentDate,
          riskCoveredTo: currentDate,
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
          policyNumber: 'BBBBBB',
          proposerName: 'BBBBBB',
          sumAssuredAmount: 1,
          extraPremium: 1,
          status: 'BBBBBB',
          gstFirstYear: 'BBBBBB',
          taxBeneficiary: 'BBBBBB',
          policyReceived: true,
          policyStartDate: currentDate.format(DATE_FORMAT),
          policyEndDate: currentDate.format(DATE_FORMAT),
          claimDone: true,
          freeHeathCheckup: true,
          zone: 'BBBBBB',
          noOfYear: 1,
          floaterSum: 'BBBBBB',
          paymentDate: currentDate.format(DATE_FORMAT),
          paToOwner: 'BBBBBB',
          riskCoveredFrom: currentDate.format(DATE_FORMAT),
          notes: 'BBBBBB',
          freeField1: 'BBBBBB',
          freeField4: 'BBBBBB',
          maturityDate: currentDate.format(DATE_FORMAT),
          uinNo: 'BBBBBB',
          lastModified: currentDate.format(DATE_FORMAT),
        },
        new Policy()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          commDate: currentDate,
          policyStartDate: currentDate,
          policyEndDate: currentDate,
          paymentDate: currentDate,
          riskCoveredFrom: currentDate,
          riskCoveredTo: currentDate,
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
          policyNumber: 'BBBBBB',
          term: 1,
          ppt: 1,
          commDate: currentDate.format(DATE_FORMAT),
          proposerName: 'BBBBBB',
          sumAssuredAmount: 1,
          premiumMode: 'BBBBBB',
          basicPremium: 1,
          extraPremium: 1,
          gst: 'BBBBBB',
          status: 'BBBBBB',
          totalPremiun: 'BBBBBB',
          gstFirstYear: 'BBBBBB',
          netPremium: 'BBBBBB',
          taxBeneficiary: 'BBBBBB',
          policyReceived: true,
          previousPolicy: 1,
          policyStartDate: currentDate.format(DATE_FORMAT),
          policyEndDate: currentDate.format(DATE_FORMAT),
          period: 'BBBBBB',
          claimDone: true,
          freeHeathCheckup: true,
          zone: 'BBBBBB',
          noOfYear: 1,
          floaterSum: 'BBBBBB',
          tpa: 'BBBBBB',
          paymentDate: currentDate.format(DATE_FORMAT),
          policyType: 'BBBBBB',
          paToOwner: 'BBBBBB',
          paToOther: 'BBBBBB',
          loading: 1,
          riskCoveredFrom: currentDate.format(DATE_FORMAT),
          riskCoveredTo: currentDate.format(DATE_FORMAT),
          notes: 'BBBBBB',
          freeField1: 'BBBBBB',
          freeField2: 'BBBBBB',
          freeField3: 'BBBBBB',
          freeField4: 'BBBBBB',
          freeField5: 'BBBBBB',
          maturityDate: currentDate.format(DATE_FORMAT),
          uinNo: 'BBBBBB',
          lastModified: currentDate.format(DATE_FORMAT),
          lastModifiedBy: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          commDate: currentDate,
          policyStartDate: currentDate,
          policyEndDate: currentDate,
          paymentDate: currentDate,
          riskCoveredFrom: currentDate,
          riskCoveredTo: currentDate,
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
        const policyArray: IPolicy[] = [{ id: 123 }, { id: 456 }, { id: 60363 }];
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
