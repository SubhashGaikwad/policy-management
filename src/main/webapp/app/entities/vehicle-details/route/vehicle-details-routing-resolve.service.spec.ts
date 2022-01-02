jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVehicleDetails, VehicleDetails } from '../vehicle-details.model';
import { VehicleDetailsService } from '../service/vehicle-details.service';

import { VehicleDetailsRoutingResolveService } from './vehicle-details-routing-resolve.service';

describe('VehicleDetails routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VehicleDetailsRoutingResolveService;
  let service: VehicleDetailsService;
  let resultVehicleDetails: IVehicleDetails | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(VehicleDetailsRoutingResolveService);
    service = TestBed.inject(VehicleDetailsService);
    resultVehicleDetails = undefined;
  });

  describe('resolve', () => {
    it('should return IVehicleDetails returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVehicleDetails).toEqual({ id: 123 });
    });

    it('should return new IVehicleDetails if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleDetails = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVehicleDetails).toEqual(new VehicleDetails());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as VehicleDetails })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleDetails = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVehicleDetails).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
