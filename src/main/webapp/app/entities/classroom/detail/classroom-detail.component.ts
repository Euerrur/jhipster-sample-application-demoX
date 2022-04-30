import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassroom } from '../classroom.model';

@Component({
  selector: 'jhi-classroom-detail',
  templateUrl: './classroom-detail.component.html',
})
export class ClassroomDetailComponent implements OnInit {
  classroom: IClassroom | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classroom }) => {
      this.classroom = classroom;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
