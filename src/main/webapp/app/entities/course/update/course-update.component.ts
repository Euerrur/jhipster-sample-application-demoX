import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICourse, Course } from '../course.model';
import { CourseService } from '../service/course.service';
import { IClassd } from 'app/entities/classd/classd.model';
import { ClassdService } from 'app/entities/classd/service/classd.service';

@Component({
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html',
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;

  classdsSharedCollection: IClassd[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    introduce: [null, [Validators.required]],
    classTime: [null, [Validators.required]],
    classds: [],
  });

  constructor(
    protected courseService: CourseService,
    protected classdService: ClassdService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ course }) => {
      if (course.id === undefined) {
        const today = dayjs().startOf('day');
        course.classTime = today;
      }

      this.updateForm(course);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const course = this.createFromForm();
    if (course.id !== undefined) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
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

  protected updateForm(course: ICourse): void {
    this.editForm.patchValue({
      id: course.id,
      name: course.name,
      introduce: course.introduce,
      classTime: course.classTime ? course.classTime.format(DATE_TIME_FORMAT) : null,
      classds: course.classds,
    });

    this.classdsSharedCollection = this.classdService.addClassdToCollectionIfMissing(
      this.classdsSharedCollection,
      ...(course.classds ?? [])
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

  protected createFromForm(): ICourse {
    return {
      ...new Course(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      introduce: this.editForm.get(['introduce'])!.value,
      classTime: this.editForm.get(['classTime'])!.value ? dayjs(this.editForm.get(['classTime'])!.value, DATE_TIME_FORMAT) : undefined,
      classds: this.editForm.get(['classds'])!.value,
    };
  }
}
