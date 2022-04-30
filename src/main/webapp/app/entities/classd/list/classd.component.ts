import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassd } from '../classd.model';
import { ClassdService } from '../service/classd.service';
import { ClassdDeleteDialogComponent } from '../delete/classd-delete-dialog.component';

@Component({
  selector: 'jhi-classd',
  templateUrl: './classd.component.html',
})
export class ClassdComponent implements OnInit {
  classds?: IClassd[];
  isLoading = false;

  constructor(protected classdService: ClassdService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.classdService.query().subscribe({
      next: (res: HttpResponse<IClassd[]>) => {
        this.isLoading = false;
        this.classds = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IClassd): number {
    return item.id!;
  }

  delete(classd: IClassd): void {
    const modalRef = this.modalService.open(ClassdDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.classd = classd;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
