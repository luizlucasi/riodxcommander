import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBand } from 'app/shared/model/band.model';
import { BandService } from './band.service';
import { BandDeleteDialogComponent } from './band-delete-dialog.component';

@Component({
  selector: 'jhi-band',
  templateUrl: './band.component.html'
})
export class BandComponent implements OnInit, OnDestroy {
  bands?: IBand[];
  eventSubscriber?: Subscription;

  constructor(protected bandService: BandService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.bandService.query().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBands();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBand): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBands(): void {
    this.eventSubscriber = this.eventManager.subscribe('bandListModification', () => this.loadAll());
  }

  delete(band: IBand): void {
    const modalRef = this.modalService.open(BandDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.band = band;
  }
}
