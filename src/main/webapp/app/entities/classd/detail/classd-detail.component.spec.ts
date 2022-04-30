import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClassdDetailComponent } from './classd-detail.component';

describe('Classd Management Detail Component', () => {
  let comp: ClassdDetailComponent;
  let fixture: ComponentFixture<ClassdDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClassdDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ classd: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ClassdDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClassdDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load classd on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.classd).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
