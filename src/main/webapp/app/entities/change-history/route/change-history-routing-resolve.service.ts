import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChangeHistory, ChangeHistory } from '../change-history.model';
import { ChangeHistoryService } from '../service/change-history.service';

@Injectable({ providedIn: 'root' })
export class ChangeHistoryRoutingResolveService implements Resolve<IChangeHistory> {
  constructor(protected service: ChangeHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChangeHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((changeHistory: HttpResponse<ChangeHistory>) => {
          if (changeHistory.body) {
            return of(changeHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChangeHistory());
  }
}
