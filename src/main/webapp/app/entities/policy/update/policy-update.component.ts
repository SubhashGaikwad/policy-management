import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPolicy, Policy } from '../policy.model';
import { PolicyService } from '../service/policy.service';
import { IUsers } from 'app/entities/users/users.model';
import { UsersService } from 'app/entities/users/service/users.service';
import { PolicyStatus } from 'app/entities/enumerations/policy-status.model';

@Component({
  selector: 'jhi-policy-update',
  templateUrl: './policy-update.component.html',
})
export class PolicyUpdateComponent implements OnInit {
  isSaving = false;
  policyStatusValues = Object.keys(PolicyStatus);

  usersSharedCollection: IUsers[] = [];

  editForm = this.fb.group({
    id: [],
    policyAmount: [],
    instalmentAmount: [],
    term: [],
    instalmentPeriod: [],
    instalmentDate: [],
    status: [],
    dateStart: [null, [Validators.required]],
    dateEnd: [null, [Validators.required]],
    maturityDate: [null, [Validators.required]],
    uinNo: [],
    lastModified: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.required]],
    users: [],
  });

  constructor(
    protected policyService: PolicyService,
    protected usersService: UsersService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ policy }) => {
      if (policy.id === undefined) {
        const today = dayjs().startOf('day');
        policy.dateStart = today;
        policy.dateEnd = today;
        policy.maturityDate = today;
        policy.lastModified = today;
      }

      this.updateForm(policy);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const policy = this.createFromForm();
    if (policy.id !== undefined) {
      this.subscribeToSaveResponse(this.policyService.update(policy));
    } else {
      this.subscribeToSaveResponse(this.policyService.create(policy));
    }
  }

  trackUsersById(index: number, item: IUsers): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPolicy>>): void {
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

  protected updateForm(policy: IPolicy): void {
    this.editForm.patchValue({
      id: policy.id,
      policyAmount: policy.policyAmount,
      instalmentAmount: policy.instalmentAmount,
      term: policy.term,
      instalmentPeriod: policy.instalmentPeriod,
      instalmentDate: policy.instalmentDate,
      status: policy.status,
      dateStart: policy.dateStart ? policy.dateStart.format(DATE_TIME_FORMAT) : null,
      dateEnd: policy.dateEnd ? policy.dateEnd.format(DATE_TIME_FORMAT) : null,
      maturityDate: policy.maturityDate ? policy.maturityDate.format(DATE_TIME_FORMAT) : null,
      uinNo: policy.uinNo,
      lastModified: policy.lastModified ? policy.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: policy.lastModifiedBy,
      users: policy.users,
    });

    this.usersSharedCollection = this.usersService.addUsersToCollectionIfMissing(this.usersSharedCollection, policy.users);
  }

  protected loadRelationshipsOptions(): void {
    this.usersService
      .query()
      .pipe(map((res: HttpResponse<IUsers[]>) => res.body ?? []))
      .pipe(map((users: IUsers[]) => this.usersService.addUsersToCollectionIfMissing(users, this.editForm.get('users')!.value)))
      .subscribe((users: IUsers[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPolicy {
    return {
      ...new Policy(),
      id: this.editForm.get(['id'])!.value,
      policyAmount: this.editForm.get(['policyAmount'])!.value,
      instalmentAmount: this.editForm.get(['instalmentAmount'])!.value,
      term: this.editForm.get(['term'])!.value,
      instalmentPeriod: this.editForm.get(['instalmentPeriod'])!.value,
      instalmentDate: this.editForm.get(['instalmentDate'])!.value,
      status: this.editForm.get(['status'])!.value,
      dateStart: this.editForm.get(['dateStart'])!.value ? dayjs(this.editForm.get(['dateStart'])!.value, DATE_TIME_FORMAT) : undefined,
      dateEnd: this.editForm.get(['dateEnd'])!.value ? dayjs(this.editForm.get(['dateEnd'])!.value, DATE_TIME_FORMAT) : undefined,
      maturityDate: this.editForm.get(['maturityDate'])!.value
        ? dayjs(this.editForm.get(['maturityDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      uinNo: this.editForm.get(['uinNo'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      users: this.editForm.get(['users'])!.value,
    };
  }
}
