import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ClassroomComponent } from './list/classroom.component';
import { ClassroomDetailComponent } from './detail/classroom-detail.component';
import { ClassroomUpdateComponent } from './update/classroom-update.component';
import { ClassroomDeleteDialogComponent } from './delete/classroom-delete-dialog.component';
import { ClassroomRoutingModule } from './route/classroom-routing.module';

@NgModule({
  imports: [SharedModule, ClassroomRoutingModule],
  declarations: [ClassroomComponent, ClassroomDetailComponent, ClassroomUpdateComponent, ClassroomDeleteDialogComponent],
  entryComponents: [ClassroomDeleteDialogComponent],
})
export class ClassroomModule {}
