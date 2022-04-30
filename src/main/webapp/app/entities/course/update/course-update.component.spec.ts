import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CourseService } from '../service/course.service';
import { ICourse, Course } from '../course.model';
import { IClassd } from 'app/entities/classd/classd.model';
import { ClassdService } from 'app/entities/classd/service/classd.service';

import { CourseUpdateComponent } from './course-update.component';

describe('Course Management Update Component', () => {
  let comp: CourseUpdateComponent;
  let fixture: ComponentFixture<CourseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let courseService: CourseService;
  let classdService: ClassdService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CourseUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CourseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    courseService = TestBed.inject(CourseService);
    classdService = TestBed.inject(ClassdService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Classd query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const classds: IClassd[] = [{ id: 17156 }];
      course.classds = classds;

      const classdCollection: IClassd[] = [{ id: 84239 }];
      jest.spyOn(classdService, 'query').mockReturnValue(of(new HttpResponse({ body: classdCollection })));
      const additionalClassds = [...classds];
      const expectedCollection: IClassd[] = [...additionalClassds, ...classdCollection];
      jest.spyOn(classdService, 'addClassdToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(classdService.query).toHaveBeenCalled();
      expect(classdService.addClassdToCollectionIfMissing).toHaveBeenCalledWith(classdCollection, ...additionalClassds);
      expect(comp.classdsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const course: ICourse = { id: 456 };
      const classds: IClassd = { id: 84691 };
      course.classds = [classds];

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(course));
      expect(comp.classdsSharedCollection).toContain(classds);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Course>>();
      const course = { id: 123 };
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(courseService.update).toHaveBeenCalledWith(course);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Course>>();
      const course = new Course();
      jest.spyOn(courseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(courseService.create).toHaveBeenCalledWith(course);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Course>>();
      const course = { id: 123 };
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(courseService.update).toHaveBeenCalledWith(course);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClassdById', () => {
      it('Should return tracked Classd primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClassdById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedClassd', () => {
      it('Should return option if no Classd is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedClassd(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Classd for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedClassd(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Classd is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedClassd(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
