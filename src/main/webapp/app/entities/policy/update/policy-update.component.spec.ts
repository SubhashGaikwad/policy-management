jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PolicyService } from '../service/policy.service';
import { IPolicy, Policy } from '../policy.model';
import { IUsers } from 'app/entities/users/users.model';
import { UsersService } from 'app/entities/users/service/users.service';

import { PolicyUpdateComponent } from './policy-update.component';

describe('Policy Management Update Component', () => {
  let comp: PolicyUpdateComponent;
  let fixture: ComponentFixture<PolicyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let policyService: PolicyService;
  let usersService: UsersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PolicyUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PolicyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PolicyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    policyService = TestBed.inject(PolicyService);
    usersService = TestBed.inject(UsersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Users query and add missing value', () => {
      const policy: IPolicy = { id: 456 };
      const users: IUsers = { id: 75345 };
      policy.users = users;

      const usersCollection: IUsers[] = [{ id: 37744 }];
      jest.spyOn(usersService, 'query').mockReturnValue(of(new HttpResponse({ body: usersCollection })));
      const additionalUsers = [users];
      const expectedCollection: IUsers[] = [...additionalUsers, ...usersCollection];
      jest.spyOn(usersService, 'addUsersToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ policy });
      comp.ngOnInit();

      expect(usersService.query).toHaveBeenCalled();
      expect(usersService.addUsersToCollectionIfMissing).toHaveBeenCalledWith(usersCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const policy: IPolicy = { id: 456 };
      const users: IUsers = { id: 38218 };
      policy.users = users;

      activatedRoute.data = of({ policy });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(policy));
      expect(comp.usersSharedCollection).toContain(users);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Policy>>();
      const policy = { id: 123 };
      jest.spyOn(policyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ policy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: policy }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(policyService.update).toHaveBeenCalledWith(policy);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Policy>>();
      const policy = new Policy();
      jest.spyOn(policyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ policy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: policy }));
      saveSubject.complete();

      // THEN
      expect(policyService.create).toHaveBeenCalledWith(policy);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Policy>>();
      const policy = { id: 123 };
      jest.spyOn(policyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ policy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(policyService.update).toHaveBeenCalledWith(policy);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUsersById', () => {
      it('Should return tracked Users primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsersById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
