import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChangeHistoryDetailComponent } from './change-history-detail.component';

describe('ChangeHistory Management Detail Component', () => {
  let comp: ChangeHistoryDetailComponent;
  let fixture: ComponentFixture<ChangeHistoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChangeHistoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ changeHistory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ChangeHistoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ChangeHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load changeHistory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.changeHistory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
