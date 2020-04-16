import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IModem } from 'app/shared/model/modem.model';
import { ModemService } from './modem.service';
import { ModemDeleteDialogComponent } from './modem-delete-dialog.component';

@Component({
  selector: 'jhi-modem',
  templateUrl: './modem.component.html'
})
export class ModemComponent implements OnInit, OnDestroy {
  modems?: IModem[];
  eventSubscriber?: Subscription;

  constructor(protected modemService: ModemService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.modemService.query().subscribe((res: HttpResponse<IModem[]>) => (this.modems = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInModems();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IModem): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInModems(): void {
    this.eventSubscriber = this.eventManager.subscribe('modemListModification', () => this.loadAll());
  }

  delete(modem: IModem): void {
    const modalRef = this.modalService.open(ModemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.modem = modem;
  }
}
