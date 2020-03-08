import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICommand } from 'app/shared/model/command.model';
import { CommandService } from './command.service';

@Component({
  templateUrl: './command-delete-dialog.component.html'
})
export class CommandDeleteDialogComponent {
  command?: ICommand;

  constructor(protected commandService: CommandService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.commandService.delete(id).subscribe(() => {
      this.eventManager.broadcast('commandListModification');
      this.activeModal.close();
    });
  }
}
