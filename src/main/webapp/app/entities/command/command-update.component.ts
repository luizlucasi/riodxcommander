import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ICommand, Command } from 'app/shared/model/command.model';
import { CommandService } from './command.service';
import { IOperator } from 'app/shared/model/operator.model';
import { OperatorService } from 'app/entities/operator/operator.service';
import { IBand } from 'app/shared/model/band.model';
import { BandService } from 'app/entities/band/band.service';

type SelectableEntity = IOperator | IBand;

@Component({
  selector: 'jhi-command-update',
  templateUrl: './command-update.component.html'
})
export class CommandUpdateComponent implements OnInit {
  isSaving = false;
  operators: IOperator[] = [];
  bands: IBand[] = [];

  editForm = this.fb.group({
    id: [],
    call: [null, [Validators.required]],
    inUse: [],
    operator: [],
    bands: []
  });

  constructor(
    protected commandService: CommandService,
    protected operatorService: OperatorService,
    protected bandService: BandService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ command }) => {
      this.updateForm(command);

      this.operatorService
        .query({ 'commandId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IOperator[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IOperator[]) => {
          if (!command.operator || !command.operator.id) {
            this.operators = resBody;
          } else {
            this.operatorService
              .find(command.operator.id)
              .pipe(
                map((subRes: HttpResponse<IOperator>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IOperator[]) => (this.operators = concatRes));
          }
        });

      this.bandService.query().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));
    });
  }

  updateForm(command: ICommand): void {
    this.editForm.patchValue({
      id: command.id,
      call: command.call,
      inUse: command.inUse,
      operator: command.operator,
      bands: command.bands
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const command = this.createFromForm();
    if (command.id !== undefined) {
      this.subscribeToSaveResponse(this.commandService.update(command));
    } else {
      this.subscribeToSaveResponse(this.commandService.create(command));
    }
  }

  private createFromForm(): ICommand {
    return {
      ...new Command(),
      id: this.editForm.get(['id'])!.value,
      call: this.editForm.get(['call'])!.value,
      inUse: this.editForm.get(['inUse'])!.value,
      operator: this.editForm.get(['operator'])!.value,
      bands: this.editForm.get(['bands'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommand>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IBand[], option: IBand): IBand {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
