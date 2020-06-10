import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRadio } from 'app/shared/model/radio.model';
import { RadioService } from './radio.service';
import { RadioDeleteDialogComponent } from './radio-delete-dialog.component';

@Component({
  selector: 'jhi-radio',
  templateUrl: './radio.component.html',
})
export class RadioComponent implements OnInit, OnDestroy {
  radios?: IRadio[];
  eventSubscriber?: Subscription;

  constructor(protected radioService: RadioService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.radioService.query().subscribe((res: HttpResponse<IRadio[]>) => (this.radios = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRadios();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRadio): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRadios(): void {
    this.eventSubscriber = this.eventManager.subscribe('radioListModification', () => this.loadAll());
  }

  delete(radio: IRadio): void {
    const modalRef = this.modalService.open(RadioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.radio = radio;
  }
}
