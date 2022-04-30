import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassroomComponent } from '../list/classroom.component';
import { ClassroomDetailComponent } from '../detail/classroom-detail.component';
import { ClassroomUpdateComponent } from '../update/classroom-update.component';
import { ClassroomRoutingResolveService } from './classroom-routing-resolve.service';

const classroomRoute: Routes = [
  {
    path: '',
    component: ClassroomComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassroomDetailComponent,
    resolve: {
      classroom: ClassroomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassroomUpdateComponent,
    resolve: {
      classroom: ClassroomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassroomUpdateComponent,
    resolve: {
      classroom: ClassroomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classroomRoute)],
  exports: [RouterModule],
})
export class ClassroomRoutingModule {}
