import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassd, Classd } from '../classd.model';
import { ClassdService } from '../service/classd.service';

@Injectable({ providedIn: 'root' })
export class ClassdRoutingResolveService implements Resolve<IClassd> {
  constructor(protected service: ClassdService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassd> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classd: HttpResponse<Classd>) => {
          if (classd.body) {
            return of(classd.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Classd());
  }
}
