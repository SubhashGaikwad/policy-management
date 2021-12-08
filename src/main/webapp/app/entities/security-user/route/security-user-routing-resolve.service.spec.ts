jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISecurityUser, SecurityUser } from '../security-user.model';
import { SecurityUserService } from '../service/security-user.service';

import { SecurityUserRoutingResolveService } from './security-user-routing-resolve.service';

describe('SecurityUser routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SecurityUserRoutingResolveService;
  let service: SecurityUserService;
  let resultSecurityUser: ISecurityUser | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SecurityUserRoutingResolveService);
    service = TestBed.inject(SecurityUserService);
    resultSecurityUser = undefined;
  });

  describe('resolve', () => {
    it('should return ISecurityUser returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityUser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSecurityUser).toEqual({ id: 123 });
    });

    it('should return new ISecurityUser if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityUser = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSecurityUser).toEqual(new SecurityUser());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SecurityUser })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSecurityUser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSecurityUser).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
