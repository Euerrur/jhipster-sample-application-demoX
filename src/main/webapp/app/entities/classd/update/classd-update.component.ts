import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IClassd, Classd } from '../classd.model';
import { ClassdService } from '../service/classd.service';

@Component({
  selector: 'jhi-classd-update',
  templateUrl: './classd-update.component.html',
})
export class ClassdUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(protected classdService: ClassdService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classd }) => {
      this.updateForm(classd);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classd = this.createFromForm();
    if (classd.id !== undefined) {
      this.subscribeToSaveResponse(this.classdService.update(classd));
    } else {
      this.subscribeToSaveResponse(this.classdService.create(classd));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassd>>): void {
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

  protected updateForm(classd: IClassd): void {
    this.editForm.patchValue({
      id: classd.id,
      name: classd.name,
    });
  }

  protected createFromForm(): IClassd {
    return {
      ...new Classd(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
