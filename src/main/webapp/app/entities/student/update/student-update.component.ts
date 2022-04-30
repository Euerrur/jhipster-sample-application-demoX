import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudent, Student } from '../student.model';
import { StudentService } from '../service/student.service';
import { IClassd } from 'app/entities/classd/classd.model';
import { ClassdService } from 'app/entities/classd/service/classd.service';
import { GradeLevelType } from 'app/entities/enumerations/grade-level-type.model';

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html',
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;
  gradeLevelTypeValues = Object.keys(GradeLevelType);

  classdsSharedCollection: IClassd[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    age: [null, [Validators.required]],
    number: [null, [Validators.required]],
    grade: [null, [Validators.required]],
    classds: [],
  });

  constructor(
    protected studentService: StudentService,
    protected classdService: ClassdService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      this.updateForm(student);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  trackClassdById(_index: number, item: IClassd): number {
    return item.id!;
  }

  getSelectedClassd(option: IClassd, selectedVals?: IClassd[]): IClassd {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
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

  protected updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      name: student.name,
      age: student.age,
      number: student.number,
      grade: student.grade,
      classds: student.classds,
    });

    this.classdsSharedCollection = this.classdService.addClassdToCollectionIfMissing(
      this.classdsSharedCollection,
      ...(student.classds ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classdService
      .query()
      .pipe(map((res: HttpResponse<IClassd[]>) => res.body ?? []))
      .pipe(
        map((classds: IClassd[]) =>
          this.classdService.addClassdToCollectionIfMissing(classds, ...(this.editForm.get('classds')!.value ?? []))
        )
      )
      .subscribe((classds: IClassd[]) => (this.classdsSharedCollection = classds));
  }

  protected createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      age: this.editForm.get(['age'])!.value,
      number: this.editForm.get(['number'])!.value,
      grade: this.editForm.get(['grade'])!.value,
      classds: this.editForm.get(['classds'])!.value,
    };
  }
}
