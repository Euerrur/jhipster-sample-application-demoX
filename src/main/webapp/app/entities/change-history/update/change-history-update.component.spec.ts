import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ChangeHistoryService } from '../service/change-history.service';
import { IChangeHistory, ChangeHistory } from '../change-history.model';

import { ChangeHistoryUpdateComponent } from './change-history-update.component';

describe('ChangeHistory Management Update Component', () => {
  let comp: ChangeHistoryUpdateComponent;
  let fixture: ComponentFixture<ChangeHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let changeHistoryService: ChangeHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ChangeHistoryUpdateComponent],
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
      .overrideTemplate(ChangeHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChangeHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    changeHistoryService = TestBed.inject(ChangeHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const changeHistory: IChangeHistory = { id: 456 };

      activatedRoute.data = of({ changeHistory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(changeHistory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ChangeHistory>>();
      const changeHistory = { id: 123 };
      jest.spyOn(changeHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ changeHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: changeHistory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(changeHistoryService.update).toHaveBeenCalledWith(changeHistory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ChangeHistory>>();
      const changeHistory = new ChangeHistory();
      jest.spyOn(changeHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ changeHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: changeHistory }));
      saveSubject.complete();

      // THEN
      expect(changeHistoryService.create).toHaveBeenCalledWith(changeHistory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ChangeHistory>>();
      const changeHistory = { id: 123 };
      jest.spyOn(changeHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ changeHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(changeHistoryService.update).toHaveBeenCalledWith(changeHistory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
