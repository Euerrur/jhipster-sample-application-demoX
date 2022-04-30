import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClassdService } from '../service/classd.service';
import { IClassd, Classd } from '../classd.model';

import { ClassdUpdateComponent } from './classd-update.component';

describe('Classd Management Update Component', () => {
  let comp: ClassdUpdateComponent;
  let fixture: ComponentFixture<ClassdUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classdService: ClassdService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ClassdUpdateComponent],
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
      .overrideTemplate(ClassdUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassdUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classdService = TestBed.inject(ClassdService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const classd: IClassd = { id: 456 };

      activatedRoute.data = of({ classd });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(classd));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classd>>();
      const classd = { id: 123 };
      jest.spyOn(classdService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classd });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classd }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(classdService.update).toHaveBeenCalledWith(classd);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classd>>();
      const classd = new Classd();
      jest.spyOn(classdService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classd });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classd }));
      saveSubject.complete();

      // THEN
      expect(classdService.create).toHaveBeenCalledWith(classd);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classd>>();
      const classd = { id: 123 };
      jest.spyOn(classdService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classd });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classdService.update).toHaveBeenCalledWith(classd);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
