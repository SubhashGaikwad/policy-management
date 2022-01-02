import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRider, Rider } from '../rider.model';
import { RiderService } from '../service/rider.service';

@Component({
  selector: 'jhi-rider-update',
  templateUrl: './rider-update.component.html',
})
export class RiderUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    commDate: [],
    sum: [],
    term: [],
    ppt: [null, []],
    premium: [],
    lastModified: [null, [Validators.required]],
    lastModifiedBy: [null, [Validators.required]],
  });

  constructor(protected riderService: RiderService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rider }) => {
      if (rider.id === undefined) {
        const today = dayjs().startOf('day');
        rider.commDate = today;
        rider.lastModified = today;
      }

      this.updateForm(rider);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rider = this.createFromForm();
    if (rider.id !== undefined) {
      this.subscribeToSaveResponse(this.riderService.update(rider));
    } else {
      this.subscribeToSaveResponse(this.riderService.create(rider));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRider>>): void {
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

  protected updateForm(rider: IRider): void {
    this.editForm.patchValue({
      id: rider.id,
      name: rider.name,
      commDate: rider.commDate ? rider.commDate.format(DATE_TIME_FORMAT) : null,
      sum: rider.sum,
      term: rider.term,
      ppt: rider.ppt,
      premium: rider.premium,
      lastModified: rider.lastModified ? rider.lastModified.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: rider.lastModifiedBy,
    });
  }

  protected createFromForm(): IRider {
    return {
      ...new Rider(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      commDate: this.editForm.get(['commDate'])!.value ? dayjs(this.editForm.get(['commDate'])!.value, DATE_TIME_FORMAT) : undefined,
      sum: this.editForm.get(['sum'])!.value,
      term: this.editForm.get(['term'])!.value,
      ppt: this.editForm.get(['ppt'])!.value,
      premium: this.editForm.get(['premium'])!.value,
      lastModified: this.editForm.get(['lastModified'])!.value
        ? dayjs(this.editForm.get(['lastModified'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
    };
  }
}
