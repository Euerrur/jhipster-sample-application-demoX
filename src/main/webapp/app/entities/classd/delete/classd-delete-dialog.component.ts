import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassd } from '../classd.model';
import { ClassdService } from '../service/classd.service';

@Component({
  templateUrl: './classd-delete-dialog.component.html',
})
export class ClassdDeleteDialogComponent {
  classd?: IClassd;

  constructor(protected classdService: ClassdService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classdService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
