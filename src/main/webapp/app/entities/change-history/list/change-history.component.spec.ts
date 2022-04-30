import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ChangeHistoryService } from '../service/change-history.service';

import { ChangeHistoryComponent } from './change-history.component';

describe('ChangeHistory Management Component', () => {
  let comp: ChangeHistoryComponent;
  let fixture: ComponentFixture<ChangeHistoryComponent>;
  let service: ChangeHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ChangeHistoryComponent],
    })
      .overrideTemplate(ChangeHistoryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChangeHistoryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ChangeHistoryService);

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
    expect(comp.changeHistories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
