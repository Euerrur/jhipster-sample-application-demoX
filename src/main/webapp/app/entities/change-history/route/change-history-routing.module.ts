import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChangeHistoryComponent } from '../list/change-history.component';
import { ChangeHistoryDetailComponent } from '../detail/change-history-detail.component';
import { ChangeHistoryUpdateComponent } from '../update/change-history-update.component';
import { ChangeHistoryRoutingResolveService } from './change-history-routing-resolve.service';

const changeHistoryRoute: Routes = [
  {
    path: '',
    component: ChangeHistoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChangeHistoryDetailComponent,
    resolve: {
      changeHistory: ChangeHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChangeHistoryUpdateComponent,
    resolve: {
      changeHistory: ChangeHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChangeHistoryUpdateComponent,
    resolve: {
      changeHistory: ChangeHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(changeHistoryRoute)],
  exports: [RouterModule],
})
export class ChangeHistoryRoutingModule {}
