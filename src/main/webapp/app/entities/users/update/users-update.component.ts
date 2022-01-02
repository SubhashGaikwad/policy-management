import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUsers, Users } from '../users.model';
import { UsersService } from '../service/users.service';
import { IUsersType } from 'app/entities/users-type/users-type.model';
import { UsersTypeService } from 'app/entities/users-type/service/users-type.service';
import { StatusInd } from 'app/entities/enumerations/status-ind.model';

@Component({
  selector: 'jhi-users-update',
  templateUrl: './users-update.component.html',
})
export class UsersUpdateComponent implements OnInit {
  isSaving = false;
  statusIndValues = Object.keys(StatusInd);

  usersTypesCollection: IUsersType[] = [];

  editForm = this.fb.group({
    id: [],
    groupCode: [],
    groupHeadName: [],
    firstName: [],
    lastName: [],
    birthDate: [null, [Validators.required]],
    marriageDate: [null, [Validators.required]],
    userTypeId: [],
    username: [null, [Validators.required]],
    password: [null, [Validators.required]],
    email: [null, []],
    imageUrl: [],
    status: [],
    activated: [null, [Validators.required]],
    licenceExpiryDate: [],
    mobileNo: [],
    aadharCardNuber: [],
    pancardNumber: [],
    oneTimePassword: [],
    otpExpiryTime: [],
    lastModified: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.required]],
    usersType: [],
  });

  constructor(
    protected usersService: UsersService,
    protected usersTypeService: UsersTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ users }) => {
      if (users.id === undefined) {
        const today = dayjs().startOf('day');
        users.birthDate = today;
        users.marriageDate = today;
        users.licenceExpiryDate = today;
        users.otpExpiryTime = today;
        users.lastModified = today;
      }

      this.updateForm(users);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const users = this.createFromForm();
    if (users.id !== undefined) {
      this.subscribeToSaveResponse(this.usersService.update(users));
    } else {
      this.subscribeToSaveResponse(this.usersService.create(users));
    }
  }

  trackUsersTypeById(index: number, item: IUsersType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsers>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(users: IUsers): void {
    this.editForm.patchValue({
      id: users.id,
      groupCode: users.groupCode,
      groupHeadName: users.groupHeadName,
      firstName: users.firstName,
      lastName: users.lastName,
      birthDate: users.birthDate ? users.birthDate.format(DATE_TIME_FORMAT) : null,
      marriageDate: users.marriageDate ? users.marriageDate.format(DATE_TIME_FORMAT) : null,
      userTypeId: users.userTypeId,
      username: users.username,
      password: users.password,
      email: users.email,
      imageUrl: users.imageUrl,
      status: users.status,
      activated: users.activated,
      licenceExpiryDate: users.licenceExpiryDate ? users.licenceExpiryDate.format(DATE_TIME_FORMAT) : null,
      mobileNo: users.mobileNo,
      aadharCardNuber: users.aadharCardNuber,
      pancardNumber: users.pancardNumber,
      oneTimePassword: users.oneTimePassword,
      otpExpiryTime: users.otpExpiryTime ? users.otpExpiryTime.format(DATE_TIME_FORMAT) : null,
      lastModified: users.lastModified ? users.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: users.lastModifiedBy,
      usersType: users.usersType,
    });

    this.usersTypesCollection = this.usersTypeService.addUsersTypeToCollectionIfMissing(this.usersTypesCollection, users.usersType);
  }

  protected loadRelationshipsOptions(): void {
    this.usersTypeService
      .query({ 'usersId.specified': 'false' })
      .pipe(map((res: HttpResponse<IUsersType[]>) => res.body ?? []))
      .pipe(
        map((usersTypes: IUsersType[]) =>
          this.usersTypeService.addUsersTypeToCollectionIfMissing(usersTypes, this.editForm.get('usersType')!.value)
        )
      )
      .subscribe((usersTypes: IUsersType[]) => (this.usersTypesCollection = usersTypes));
  }

  protected createFromForm(): IUsers {
    return {
      ...new Users(),
      id: this.editForm.get(['id'])!.value,
      groupCode: this.editForm.get(['groupCode'])!.value,
      groupHeadName: this.editForm.get(['groupHeadName'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value ? dayjs(this.editForm.get(['birthDate'])!.value, DATE_TIME_FORMAT) : undefined,
      marriageDate: this.editForm.get(['marriageDate'])!.value
        ? dayjs(this.editForm.get(['marriageDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      userTypeId: this.editForm.get(['userTypeId'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      email: this.editForm.get(['email'])!.value,
      imageUrl: this.editForm.get(['imageUrl'])!.value,
      status: this.editForm.get(['status'])!.value,
      activated: this.editForm.get(['activated'])!.value,
      licenceExpiryDate: this.editForm.get(['licenceExpiryDate'])!.value
        ? dayjs(this.editForm.get(['licenceExpiryDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      mobileNo: this.editForm.get(['mobileNo'])!.value,
      aadharCardNuber: this.editForm.get(['aadharCardNuber'])!.value,
      pancardNumber: this.editForm.get(['pancardNumber'])!.value,
      oneTimePassword: this.editForm.get(['oneTimePassword'])!.value,
      otpExpiryTime: this.editForm.get(['otpExpiryTime'])!.value
        ? dayjs(this.editForm.get(['otpExpiryTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      usersType: this.editForm.get(['usersType'])!.value,
    };
  }
}
