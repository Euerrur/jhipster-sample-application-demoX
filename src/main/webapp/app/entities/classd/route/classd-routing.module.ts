import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassdComponent } from '../list/classd.component';
import { ClassdDetailComponent } from '../detail/classd-detail.component';
import { ClassdUpdateComponent } from '../update/classd-update.component';
import { ClassdRoutingResolveService } from './classd-routing-resolve.service';

const classdRoute: Routes = [
  {
    path: '',
    component: ClassdComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassdDetailComponent,
    resolve: {
      classd: ClassdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassdUpdateComponent,
    resolve: {
      classd: ClassdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassdUpdateComponent,
    resolve: {
      classd: ClassdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classdRoute)],
  exports: [RouterModule],
})
export class ClassdRoutingModule {}
