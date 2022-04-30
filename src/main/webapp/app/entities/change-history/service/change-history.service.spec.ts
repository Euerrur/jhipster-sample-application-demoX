import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IChangeHistory, ChangeHistory } from '../change-history.model';

import { ChangeHistoryService } from './change-history.service';

describe('ChangeHistory Service', () => {
  let service: ChangeHistoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IChangeHistory;
  let expectedResult: IChangeHistory | IChangeHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChangeHistoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      describe: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ChangeHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ChangeHistory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ChangeHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          describe: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ChangeHistory', () => {
      const patchObject = Object.assign(
        {
          describe: 'BBBBBB',
        },
        new ChangeHistory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ChangeHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          describe: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ChangeHistory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addChangeHistoryToCollectionIfMissing', () => {
      it('should add a ChangeHistory to an empty array', () => {
        const changeHistory: IChangeHistory = { id: 123 };
        expectedResult = service.addChangeHistoryToCollectionIfMissing([], changeHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(changeHistory);
      });

      it('should not add a ChangeHistory to an array that contains it', () => {
        const changeHistory: IChangeHistory = { id: 123 };
        const changeHistoryCollection: IChangeHistory[] = [
          {
            ...changeHistory,
          },
          { id: 456 },
        ];
        expectedResult = service.addChangeHistoryToCollectionIfMissing(changeHistoryCollection, changeHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ChangeHistory to an array that doesn't contain it", () => {
        const changeHistory: IChangeHistory = { id: 123 };
        const changeHistoryCollection: IChangeHistory[] = [{ id: 456 }];
        expectedResult = service.addChangeHistoryToCollectionIfMissing(changeHistoryCollection, changeHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(changeHistory);
      });

      it('should add only unique ChangeHistory to an array', () => {
        const changeHistoryArray: IChangeHistory[] = [{ id: 123 }, { id: 456 }, { id: 24232 }];
        const changeHistoryCollection: IChangeHistory[] = [{ id: 123 }];
        expectedResult = service.addChangeHistoryToCollectionIfMissing(changeHistoryCollection, ...changeHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const changeHistory: IChangeHistory = { id: 123 };
        const changeHistory2: IChangeHistory = { id: 456 };
        expectedResult = service.addChangeHistoryToCollectionIfMissing([], changeHistory, changeHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(changeHistory);
        expect(expectedResult).toContain(changeHistory2);
      });

      it('should accept null and undefined values', () => {
        const changeHistory: IChangeHistory = { id: 123 };
        expectedResult = service.addChangeHistoryToCollectionIfMissing([], null, changeHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(changeHistory);
      });

      it('should return initial array if no ChangeHistory is added', () => {
        const changeHistoryCollection: IChangeHistory[] = [{ id: 123 }];
        expectedResult = service.addChangeHistoryToCollectionIfMissing(changeHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(changeHistoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
