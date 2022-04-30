import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChangeHistory } from '../change-history.model';
import { ChangeHistoryService } from '../service/change-history.service';

@Component({
  templateUrl: './change-history-delete-dialog.component.html',
})
export class ChangeHistoryDeleteDialogComponent {
  changeHistory?: IChangeHistory;

  constructor(protected changeHistoryService: ChangeHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.changeHistoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
