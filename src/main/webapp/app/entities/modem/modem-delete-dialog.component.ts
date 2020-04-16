import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IModem } from 'app/shared/model/modem.model';
import { ModemService } from './modem.service';

@Component({
  templateUrl: './modem-delete-dialog.component.html'
})
export class ModemDeleteDialogComponent {
  modem?: IModem;

  constructor(protected modemService: ModemService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.modemService.delete(id).subscribe(() => {
      this.eventManager.broadcast('modemListModification');
      this.activeModal.close();
    });
  }
}
