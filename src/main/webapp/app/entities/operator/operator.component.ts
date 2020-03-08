import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOperator } from 'app/shared/model/operator.model';
import { OperatorService } from './operator.service';
import { OperatorDeleteDialogComponent } from './operator-delete-dialog.component';

@Component({
  selector: 'jhi-operator',
  templateUrl: './operator.component.html'
})
export class OperatorComponent implements OnInit, OnDestroy {
  operators?: IOperator[];
  eventSubscriber?: Subscription;

  constructor(protected operatorService: OperatorService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.operatorService.query().subscribe((res: HttpResponse<IOperator[]>) => (this.operators = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOperators();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOperator): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOperators(): void {
    this.eventSubscriber = this.eventManager.subscribe('operatorListModification', () => this.loadAll());
  }

  delete(operator: IOperator): void {
    const modalRef = this.modalService.open(OperatorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.operator = operator;
  }
}
