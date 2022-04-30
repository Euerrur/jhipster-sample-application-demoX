import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ClassdComponent } from './list/classd.component';
import { ClassdDetailComponent } from './detail/classd-detail.component';
import { ClassdUpdateComponent } from './update/classd-update.component';
import { ClassdDeleteDialogComponent } from './delete/classd-delete-dialog.component';
import { ClassdRoutingModule } from './route/classd-routing.module';

@NgModule({
  imports: [SharedModule, ClassdRoutingModule],
  declarations: [ClassdComponent, ClassdDetailComponent, ClassdUpdateComponent, ClassdDeleteDialogComponent],
  entryComponents: [ClassdDeleteDialogComponent],
})
export class ClassdModule {}
