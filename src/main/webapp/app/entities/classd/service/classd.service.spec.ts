import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClassd, Classd } from '../classd.model';

import { ClassdService } from './classd.service';

describe('Classd Service', () => {
  let service: ClassdService;
  let httpMock: HttpTestingController;
  let elemDefault: IClassd;
  let expectedResult: IClassd | IClassd[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClassdService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a Classd', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Classd()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Classd', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Classd', () => {
      const patchObject = Object.assign({}, new Classd());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Classd', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a Classd', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addClassdToCollectionIfMissing', () => {
      it('should add a Classd to an empty array', () => {
        const classd: IClassd = { id: 123 };
        expectedResult = service.addClassdToCollectionIfMissing([], classd);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classd);
      });

      it('should not add a Classd to an array that contains it', () => {
        const classd: IClassd = { id: 123 };
        const classdCollection: IClassd[] = [
          {
            ...classd,
          },
          { id: 456 },
        ];
        expectedResult = service.addClassdToCollectionIfMissing(classdCollection, classd);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Classd to an array that doesn't contain it", () => {
        const classd: IClassd = { id: 123 };
        const classdCollection: IClassd[] = [{ id: 456 }];
        expectedResult = service.addClassdToCollectionIfMissing(classdCollection, classd);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classd);
      });

      it('should add only unique Classd to an array', () => {
        const classdArray: IClassd[] = [{ id: 123 }, { id: 456 }, { id: 66594 }];
        const classdCollection: IClassd[] = [{ id: 123 }];
        expectedResult = service.addClassdToCollectionIfMissing(classdCollection, ...classdArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const classd: IClassd = { id: 123 };
        const classd2: IClassd = { id: 456 };
        expectedResult = service.addClassdToCollectionIfMissing([], classd, classd2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classd);
        expect(expectedResult).toContain(classd2);
      });

      it('should accept null and undefined values', () => {
        const classd: IClassd = { id: 123 };
        expectedResult = service.addClassdToCollectionIfMissing([], null, classd, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classd);
      });

      it('should return initial array if no Classd is added', () => {
        const classdCollection: IClassd[] = [{ id: 123 }];
        expectedResult = service.addClassdToCollectionIfMissing(classdCollection, undefined, null);
        expect(expectedResult).toEqual(classdCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
