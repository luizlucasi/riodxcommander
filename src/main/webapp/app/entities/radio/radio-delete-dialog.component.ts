import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRadio } from 'app/shared/model/radio.model';
import { RadioService } from './radio.service';

@Component({
  templateUrl: './radio-delete-dialog.component.html',
})
export class RadioDeleteDialogComponent {
  radio?: IRadio;

  constructor(protected radioService: RadioService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.radioService.delete(id).subscribe(() => {
      this.eventManager.broadcast('radioListModification');
      this.activeModal.close();
    });
  }
}
