import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IChangeHistory, ChangeHistory } from '../change-history.model';
import { ChangeHistoryService } from '../service/change-history.service';

@Component({
  selector: 'jhi-change-history-update',
  templateUrl: './change-history-update.component.html',
})
export class ChangeHistoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    describe: [null, [Validators.required]],
  });

  constructor(protected changeHistoryService: ChangeHistoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ changeHistory }) => {
      this.updateForm(changeHistory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const changeHistory = this.createFromForm();
    if (changeHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.changeHistoryService.update(changeHistory));
    } else {
      this.subscribeToSaveResponse(this.changeHistoryService.create(changeHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChangeHistory>>): void {
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

  protected updateForm(changeHistory: IChangeHistory): void {
    this.editForm.patchValue({
      id: changeHistory.id,
      describe: changeHistory.describe,
    });
  }

  protected createFromForm(): IChangeHistory {
    return {
      ...new ChangeHistory(),
      id: this.editForm.get(['id'])!.value,
      describe: this.editForm.get(['describe'])!.value,
    };
  }
}
