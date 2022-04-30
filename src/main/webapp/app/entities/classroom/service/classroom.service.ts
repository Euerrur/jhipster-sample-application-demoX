import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassroom, getClassroomIdentifier } from '../classroom.model';

export type EntityResponseType = HttpResponse<IClassroom>;
export type EntityArrayResponseType = HttpResponse<IClassroom[]>;

@Injectable({ providedIn: 'root' })
export class ClassroomService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/classrooms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(classroom: IClassroom): Observable<EntityResponseType> {
    return this.http.post<IClassroom>(this.resourceUrl, classroom, { observe: 'response' });
  }

  update(classroom: IClassroom): Observable<EntityResponseType> {
    return this.http.put<IClassroom>(`${this.resourceUrl}/${getClassroomIdentifier(classroom) as number}`, classroom, {
      observe: 'response',
    });
  }

  partialUpdate(classroom: IClassroom): Observable<EntityResponseType> {
    return this.http.patch<IClassroom>(`${this.resourceUrl}/${getClassroomIdentifier(classroom) as number}`, classroom, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassroom>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassroom[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassroomToCollectionIfMissing(
    classroomCollection: IClassroom[],
    ...classroomsToCheck: (IClassroom | null | undefined)[]
  ): IClassroom[] {
    const classrooms: IClassroom[] = classroomsToCheck.filter(isPresent);
    if (classrooms.length > 0) {
      const classroomCollectionIdentifiers = classroomCollection.map(classroomItem => getClassroomIdentifier(classroomItem)!);
      const classroomsToAdd = classrooms.filter(classroomItem => {
        const classroomIdentifier = getClassroomIdentifier(classroomItem);
        if (classroomIdentifier == null || classroomCollectionIdentifiers.includes(classroomIdentifier)) {
          return false;
        }
        classroomCollectionIdentifiers.push(classroomIdentifier);
        return true;
      });
      return [...classroomsToAdd, ...classroomCollection];
    }
    return classroomCollection;
  }
}
