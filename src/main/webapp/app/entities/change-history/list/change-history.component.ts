import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IChangeHistory } from '../change-history.model';
import { ChangeHistoryService } from '../service/change-history.service';
import { ChangeHistoryDeleteDialogComponent } from '../delete/change-history-delete-dialog.component';

@Component({
  selector: 'jhi-change-history',
  templateUrl: './change-history.component.html',
})
export class ChangeHistoryComponent implements OnInit {
  changeHistories?: IChangeHistory[];
  isLoading = false;

  constructor(protected changeHistoryService: ChangeHistoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.changeHistoryService.query().subscribe({
      next: (res: HttpResponse<IChangeHistory[]>) => {
        this.isLoading = false;
        this.changeHistories = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IChangeHistory): number {
    return item.id!;
  }

  delete(changeHistory: IChangeHistory): void {
    const modalRef = this.modalService.open(ChangeHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.changeHistory = changeHistory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
