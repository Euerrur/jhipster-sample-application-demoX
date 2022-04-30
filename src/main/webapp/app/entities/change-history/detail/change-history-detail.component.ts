import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChangeHistory } from '../change-history.model';

@Component({
  selector: 'jhi-change-history-detail',
  templateUrl: './change-history-detail.component.html',
})
export class ChangeHistoryDetailComponent implements OnInit {
  changeHistory: IChangeHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ changeHistory }) => {
      this.changeHistory = changeHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
