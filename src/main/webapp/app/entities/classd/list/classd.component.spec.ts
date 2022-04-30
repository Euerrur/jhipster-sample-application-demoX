import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ClassdService } from '../service/classd.service';

import { ClassdComponent } from './classd.component';

describe('Classd Management Component', () => {
  let comp: ClassdComponent;
  let fixture: ComponentFixture<ClassdComponent>;
  let service: ClassdService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ClassdComponent],
    })
      .overrideTemplate(ClassdComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassdComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ClassdService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.classds?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
