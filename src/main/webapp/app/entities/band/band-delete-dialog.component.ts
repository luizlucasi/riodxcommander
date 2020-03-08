import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBand } from 'app/shared/model/band.model';
import { BandService } from './band.service';

@Component({
  templateUrl: './band-delete-dialog.component.html'
})
export class BandDeleteDialogComponent {
  band?: IBand;

  constructor(protected bandService: BandService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bandService.delete(id).subscribe(() => {
      this.eventManager.broadcast('bandListModification');
      this.activeModal.close();
    });
  }
}
