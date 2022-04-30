import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClassroomService } from '../service/classroom.service';
import { IClassroom, Classroom } from '../classroom.model';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

import { ClassroomUpdateComponent } from './classroom-update.component';

describe('Classroom Management Update Component', () => {
  let comp: ClassroomUpdateComponent;
  let fixture: ComponentFixture<ClassroomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classroomService: ClassroomService;
  let courseService: CourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ClassroomUpdateComponent],
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
      .overrideTemplate(ClassroomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassroomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classroomService = TestBed.inject(ClassroomService);
    courseService = TestBed.inject(CourseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Course query and add missing value', () => {
      const classroom: IClassroom = { id: 456 };
      const courses: ICourse[] = [{ id: 427 }];
      classroom.courses = courses;

      const courseCollection: ICourse[] = [{ id: 54696 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [...courses];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classroom });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, ...additionalCourses);
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classroom: IClassroom = { id: 456 };
      const courses: ICourse = { id: 59337 };
      classroom.courses = [courses];

      activatedRoute.data = of({ classroom });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(classroom));
      expect(comp.coursesSharedCollection).toContain(courses);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classroom>>();
      const classroom = { id: 123 };
      jest.spyOn(classroomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classroom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classroom }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(classroomService.update).toHaveBeenCalledWith(classroom);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classroom>>();
      const classroom = new Classroom();
      jest.spyOn(classroomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classroom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classroom }));
      saveSubject.complete();

      // THEN
      expect(classroomService.create).toHaveBeenCalledWith(classroom);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classroom>>();
      const classroom = { id: 123 };
      jest.spyOn(classroomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classroom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classroomService.update).toHaveBeenCalledWith(classroom);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCourseById', () => {
      it('Should return tracked Course primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCourseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCourse', () => {
      it('Should return option if no Course is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCourse(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Course for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCourse(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Course is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCourse(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
