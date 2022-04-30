import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassroom } from '../classroom.model';
import { ClassroomService } from '../service/classroom.service';

@Component({
  templateUrl: './classroom-delete-dialog.component.html',
})
export class ClassroomDeleteDialogComponent {
  classroom?: IClassroom;

  constructor(protected classroomService: ClassroomService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classroomService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
