jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICompanyType, CompanyType } from '../company-type.model';
import { CompanyTypeService } from '../service/company-type.service';

import { CompanyTypeRoutingResolveService } from './company-type-routing-resolve.service';

describe('CompanyType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CompanyTypeRoutingResolveService;
  let service: CompanyTypeService;
  let resultCompanyType: ICompanyType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CompanyTypeRoutingResolveService);
    service = TestBed.inject(CompanyTypeService);
    resultCompanyType = undefined;
  });

  describe('resolve', () => {
    it('should return ICompanyType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompanyType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCompanyType).toEqual({ id: 123 });
    });

    it('should return new ICompanyType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompanyType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCompanyType).toEqual(new CompanyType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CompanyType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompanyType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCompanyType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
