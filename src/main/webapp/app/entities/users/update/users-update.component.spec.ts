jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UsersService } from '../service/users.service';
import { IUsers, Users } from '../users.model';
import { IUsersType } from 'app/entities/users-type/users-type.model';
import { UsersTypeService } from 'app/entities/users-type/service/users-type.service';

import { UsersUpdateComponent } from './users-update.component';

describe('Users Management Update Component', () => {
  let comp: UsersUpdateComponent;
  let fixture: ComponentFixture<UsersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let usersService: UsersService;
  let usersTypeService: UsersTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [UsersUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(UsersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usersService = TestBed.inject(UsersService);
    usersTypeService = TestBed.inject(UsersTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call usersType query and add missing value', () => {
      const users: IUsers = { id: 456 };
      const usersType: IUsersType = { id: 51254 };
      users.usersType = usersType;

      const usersTypeCollection: IUsersType[] = [{ id: 63557 }];
      jest.spyOn(usersTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: usersTypeCollection })));
      const expectedCollection: IUsersType[] = [usersType, ...usersTypeCollection];
      jest.spyOn(usersTypeService, 'addUsersTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ users });
      comp.ngOnInit();

      expect(usersTypeService.query).toHaveBeenCalled();
      expect(usersTypeService.addUsersTypeToCollectionIfMissing).toHaveBeenCalledWith(usersTypeCollection, usersType);
      expect(comp.usersTypesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const users: IUsers = { id: 456 };
      const usersType: IUsersType = { id: 53079 };
      users.usersType = usersType;

      activatedRoute.data = of({ users });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(users));
      expect(comp.usersTypesCollection).toContain(usersType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Users>>();
      const users = { id: 123 };
      jest.spyOn(usersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ users });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: users }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(usersService.update).toHaveBeenCalledWith(users);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Users>>();
      const users = new Users();
      jest.spyOn(usersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ users });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: users }));
      saveSubject.complete();

      // THEN
      expect(usersService.create).toHaveBeenCalledWith(users);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Users>>();
      const users = { id: 123 };
      jest.spyOn(usersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ users });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usersService.update).toHaveBeenCalledWith(users);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUsersTypeById', () => {
      it('Should return tracked UsersType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsersTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
