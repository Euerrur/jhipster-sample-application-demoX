import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassd } from '../classd.model';

@Component({
  selector: 'jhi-classd-detail',
  templateUrl: './classd-detail.component.html',
})
export class ClassdDetailComponent implements OnInit {
  classd: IClassd | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classd }) => {
      this.classd = classd;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
