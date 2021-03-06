import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUsersType, UsersType } from '../users-type.model';
import { UsersTypeService } from '../service/users-type.service';

@Component({
  selector: 'jhi-users-type-update',
  templateUrl: './users-type-update.component.html',
})
export class UsersTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    lastModified: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.required]],
  });

  constructor(protected usersTypeService: UsersTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usersType }) => {
      if (usersType.id === undefined) {
        const today = dayjs().startOf('day');
        usersType.lastModified = today;
      }

      this.updateForm(usersType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usersType = this.createFromForm();
    if (usersType.id !== undefined) {
      this.subscribeToSaveResponse(this.usersTypeService.update(usersType));
    } else {
      this.subscribeToSaveResponse(this.usersTypeService.create(usersType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsersType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
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

  protected updateForm(usersType: IUsersType): void {
    this.editForm.patchValue({
      id: usersType.id,
      name: usersType.name,
      lastModified: usersType.lastModified ? usersType.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: usersType.lastModifiedBy,
    });
  }

  protected createFromForm(): IUsersType {
    return {
      ...new UsersType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
    };
  }
}
