import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassroom } from '../classroom.model';
import { ClassroomService } from '../service/classroom.service';
import { ClassroomDeleteDialogComponent } from '../delete/classroom-delete-dialog.component';

@Component({
  selector: 'jhi-classroom',
  templateUrl: './classroom.component.html',
})
export class ClassroomComponent implements OnInit {
  classrooms?: IClassroom[];
  isLoading = false;

  constructor(protected classroomService: ClassroomService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.classroomService.query().subscribe({
      next: (res: HttpResponse<IClassroom[]>) => {
        this.isLoading = false;
        this.classrooms = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IClassroom): number {
    return item.id!;
  }

  delete(classroom: IClassroom): void {
    const modalRef = this.modalService.open(ClassroomDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.classroom = classroom;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
