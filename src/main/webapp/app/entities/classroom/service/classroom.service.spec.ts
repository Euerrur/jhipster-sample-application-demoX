import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClassroom, Classroom } from '../classroom.model';

import { ClassroomService } from './classroom.service';

describe('Classroom Service', () => {
  let service: ClassroomService;
  let httpMock: HttpTestingController;
  let elemDefault: IClassroom;
  let expectedResult: IClassroom | IClassroom[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClassroomService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      address: 'AAAAAAA',
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

    it('should create a Classroom', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Classroom()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Classroom', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          address: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Classroom', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          address: 'BBBBBB',
        },
        new Classroom()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Classroom', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          address: 'BBBBBB',
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

    it('should delete a Classroom', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addClassroomToCollectionIfMissing', () => {
      it('should add a Classroom to an empty array', () => {
        const classroom: IClassroom = { id: 123 };
        expectedResult = service.addClassroomToCollectionIfMissing([], classroom);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classroom);
      });

      it('should not add a Classroom to an array that contains it', () => {
        const classroom: IClassroom = { id: 123 };
        const classroomCollection: IClassroom[] = [
          {
            ...classroom,
          },
          { id: 456 },
        ];
        expectedResult = service.addClassroomToCollectionIfMissing(classroomCollection, classroom);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Classroom to an array that doesn't contain it", () => {
        const classroom: IClassroom = { id: 123 };
        const classroomCollection: IClassroom[] = [{ id: 456 }];
        expectedResult = service.addClassroomToCollectionIfMissing(classroomCollection, classroom);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classroom);
      });

      it('should add only unique Classroom to an array', () => {
        const classroomArray: IClassroom[] = [{ id: 123 }, { id: 456 }, { id: 5363 }];
        const classroomCollection: IClassroom[] = [{ id: 123 }];
        expectedResult = service.addClassroomToCollectionIfMissing(classroomCollection, ...classroomArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const classroom: IClassroom = { id: 123 };
        const classroom2: IClassroom = { id: 456 };
        expectedResult = service.addClassroomToCollectionIfMissing([], classroom, classroom2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classroom);
        expect(expectedResult).toContain(classroom2);
      });

      it('should accept null and undefined values', () => {
        const classroom: IClassroom = { id: 123 };
        expectedResult = service.addClassroomToCollectionIfMissing([], null, classroom, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classroom);
      });

      it('should return initial array if no Classroom is added', () => {
        const classroomCollection: IClassroom[] = [{ id: 123 }];
        expectedResult = service.addClassroomToCollectionIfMissing(classroomCollection, undefined, null);
        expect(expectedResult).toEqual(classroomCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
