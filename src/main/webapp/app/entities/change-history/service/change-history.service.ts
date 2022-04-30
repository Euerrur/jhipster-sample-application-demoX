import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChangeHistory, getChangeHistoryIdentifier } from '../change-history.model';

export type EntityResponseType = HttpResponse<IChangeHistory>;
export type EntityArrayResponseType = HttpResponse<IChangeHistory[]>;

@Injectable({ providedIn: 'root' })
export class ChangeHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/change-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(changeHistory: IChangeHistory): Observable<EntityResponseType> {
    return this.http.post<IChangeHistory>(this.resourceUrl, changeHistory, { observe: 'response' });
  }

  update(changeHistory: IChangeHistory): Observable<EntityResponseType> {
    return this.http.put<IChangeHistory>(`${this.resourceUrl}/${getChangeHistoryIdentifier(changeHistory) as number}`, changeHistory, {
      observe: 'response',
    });
  }

  partialUpdate(changeHistory: IChangeHistory): Observable<EntityResponseType> {
    return this.http.patch<IChangeHistory>(`${this.resourceUrl}/${getChangeHistoryIdentifier(changeHistory) as number}`, changeHistory, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChangeHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChangeHistory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChangeHistoryToCollectionIfMissing(
    changeHistoryCollection: IChangeHistory[],
    ...changeHistoriesToCheck: (IChangeHistory | null | undefined)[]
  ): IChangeHistory[] {
    const changeHistories: IChangeHistory[] = changeHistoriesToCheck.filter(isPresent);
    if (changeHistories.length > 0) {
      const changeHistoryCollectionIdentifiers = changeHistoryCollection.map(
        changeHistoryItem => getChangeHistoryIdentifier(changeHistoryItem)!
      );
      const changeHistoriesToAdd = changeHistories.filter(changeHistoryItem => {
        const changeHistoryIdentifier = getChangeHistoryIdentifier(changeHistoryItem);
        if (changeHistoryIdentifier == null || changeHistoryCollectionIdentifiers.includes(changeHistoryIdentifier)) {
          return false;
        }
        changeHistoryCollectionIdentifiers.push(changeHistoryIdentifier);
        return true;
      });
      return [...changeHistoriesToAdd, ...changeHistoryCollection];
    }
    return changeHistoryCollection;
  }
}
