import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassd, getClassdIdentifier } from '../classd.model';

export type EntityResponseType = HttpResponse<IClassd>;
export type EntityArrayResponseType = HttpResponse<IClassd[]>;

@Injectable({ providedIn: 'root' })
export class ClassdService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/classds');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(classd: IClassd): Observable<EntityResponseType> {
    return this.http.post<IClassd>(this.resourceUrl, classd, { observe: 'response' });
  }

  update(classd: IClassd): Observable<EntityResponseType> {
    return this.http.put<IClassd>(`${this.resourceUrl}/${getClassdIdentifier(classd) as number}`, classd, { observe: 'response' });
  }

  partialUpdate(classd: IClassd): Observable<EntityResponseType> {
    return this.http.patch<IClassd>(`${this.resourceUrl}/${getClassdIdentifier(classd) as number}`, classd, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassd>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassd[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClassdToCollectionIfMissing(classdCollection: IClassd[], ...classdsToCheck: (IClassd | null | undefined)[]): IClassd[] {
    const classds: IClassd[] = classdsToCheck.filter(isPresent);
    if (classds.length > 0) {
      const classdCollectionIdentifiers = classdCollection.map(classdItem => getClassdIdentifier(classdItem)!);
      const classdsToAdd = classds.filter(classdItem => {
        const classdIdentifier = getClassdIdentifier(classdItem);
        if (classdIdentifier == null || classdCollectionIdentifiers.includes(classdIdentifier)) {
          return false;
        }
        classdCollectionIdentifiers.push(classdIdentifier);
        return true;
      });
      return [...classdsToAdd, ...classdCollection];
    }
    return classdCollection;
  }
}
