import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRadio, Radio } from 'app/shared/model/radio.model';
import { RadioService } from './radio.service';

@Component({
  selector: 'jhi-radio-update',
  templateUrl: './radio-update.component.html',
})
export class RadioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
  });

  constructor(protected radioService: RadioService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ radio }) => {
      this.updateForm(radio);
    });
  }

  updateForm(radio: IRadio): void {
    this.editForm.patchValue({
      id: radio.id,
      description: radio.description,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const radio = this.createFromForm();
    if (radio.id !== undefined) {
      this.subscribeToSaveResponse(this.radioService.update(radio));
    } else {
      this.subscribeToSaveResponse(this.radioService.create(radio));
    }
  }

  private createFromForm(): IRadio {
    return {
      ...new Radio(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRadio>>): void {
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
}
