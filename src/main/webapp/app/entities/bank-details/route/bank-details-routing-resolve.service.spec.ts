jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBankDetails, BankDetails } from '../bank-details.model';
import { BankDetailsService } from '../service/bank-details.service';

import { BankDetailsRoutingResolveService } from './bank-details-routing-resolve.service';

describe('BankDetails routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BankDetailsRoutingResolveService;
  let service: BankDetailsService;
  let resultBankDetails: IBankDetails | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(BankDetailsRoutingResolveService);
    service = TestBed.inject(BankDetailsService);
    resultBankDetails = undefined;
  });

  describe('resolve', () => {
    it('should return IBankDetails returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBankDetails).toEqual({ id: 123 });
    });

    it('should return new IBankDetails if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankDetails = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBankDetails).toEqual(new BankDetails());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BankDetails })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBankDetails).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
