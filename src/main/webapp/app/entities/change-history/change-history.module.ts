import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChangeHistoryComponent } from './list/change-history.component';
import { ChangeHistoryDetailComponent } from './detail/change-history-detail.component';
import { ChangeHistoryUpdateComponent } from './update/change-history-update.component';
import { ChangeHistoryDeleteDialogComponent } from './delete/change-history-delete-dialog.component';
import { ChangeHistoryRoutingModule } from './route/change-history-routing.module';

@NgModule({
  imports: [SharedModule, ChangeHistoryRoutingModule],
  declarations: [ChangeHistoryComponent, ChangeHistoryDetailComponent, ChangeHistoryUpdateComponent, ChangeHistoryDeleteDialogComponent],
  entryComponents: [ChangeHistoryDeleteDialogComponent],
})
export class ChangeHistoryModule {}
