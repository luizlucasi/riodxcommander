import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IModem } from 'app/shared/model/modem.model';

@Component({
  selector: 'jhi-modem-detail',
  templateUrl: './modem-detail.component.html'
})
export class ModemDetailComponent implements OnInit {
  modem: IModem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ modem }) => (this.modem = modem));
  }

  previousState(): void {
    window.history.back();
  }
}
