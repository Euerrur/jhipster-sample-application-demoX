import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClassroomDetailComponent } from './classroom-detail.component';

describe('Classroom Management Detail Component', () => {
  let comp: ClassroomDetailComponent;
  let fixture: ComponentFixture<ClassroomDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClassroomDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ classroom: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ClassroomDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClassroomDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load classroom on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.classroom).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
