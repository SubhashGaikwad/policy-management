jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPolicy, Policy } from '../policy.model';
import { PolicyService } from '../service/policy.service';

import { PolicyRoutingResolveService } from './policy-routing-resolve.service';

describe('Policy routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PolicyRoutingResolveService;
  let service: PolicyService;
  let resultPolicy: IPolicy | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PolicyRoutingResolveService);
    service = TestBed.inject(PolicyService);
    resultPolicy = undefined;
  });

  describe('resolve', () => {
    it('should return IPolicy returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPolicy = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPolicy).toEqual({ id: 123 });
    });

    it('should return new IPolicy if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPolicy = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPolicy).toEqual(new Policy());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Policy })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPolicy = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPolicy).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
