jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPremiunDetails, PremiunDetails } from '../premiun-details.model';
import { PremiunDetailsService } from '../service/premiun-details.service';

import { PremiunDetailsRoutingResolveService } from './premiun-details-routing-resolve.service';

describe('PremiunDetails routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PremiunDetailsRoutingResolveService;
  let service: PremiunDetailsService;
  let resultPremiunDetails: IPremiunDetails | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PremiunDetailsRoutingResolveService);
    service = TestBed.inject(PremiunDetailsService);
    resultPremiunDetails = undefined;
  });

  describe('resolve', () => {
    it('should return IPremiunDetails returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPremiunDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPremiunDetails).toEqual({ id: 123 });
    });

    it('should return new IPremiunDetails if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPremiunDetails = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPremiunDetails).toEqual(new PremiunDetails());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PremiunDetails })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPremiunDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPremiunDetails).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
