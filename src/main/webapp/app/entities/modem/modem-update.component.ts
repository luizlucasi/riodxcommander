import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IModem, Modem } from 'app/shared/model/modem.model';
import { ModemService } from './modem.service';

@Component({
  selector: 'jhi-modem-update',
  templateUrl: './modem-update.component.html'
})
export class ModemUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    crmEstacaoId: [],
    vsatId: [],
    vsatUid: [],
    vsatGroup: [],
    hub: [],
    name: [],
    ipAddress: [],
    status: [],
    statusDesc: [],
    objectOID: [],
    latitude: [],
    longitude: [],
    lanIpAddress: []
  });

  constructor(protected modemService: ModemService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ modem }) => {
      this.updateForm(modem);
    });
  }

  updateForm(modem: IModem): void {
    this.editForm.patchValue({
      id: modem.id,
      crmEstacaoId: modem.crmEstacaoId,
      vsatId: modem.vsatId,
      vsatUid: modem.vsatUid,
      vsatGroup: modem.vsatGroup,
      hub: modem.hub,
      name: modem.name,
      ipAddress: modem.ipAddress,
      status: modem.status,
      statusDesc: modem.statusDesc,
      objectOID: modem.objectOID,
      latitude: modem.latitude,
      longitude: modem.longitude,
      lanIpAddress: modem.lanIpAddress
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const modem = this.createFromForm();
    if (modem.id !== undefined) {
      this.subscribeToSaveResponse(this.modemService.update(modem));
    } else {
      this.subscribeToSaveResponse(this.modemService.create(modem));
    }
  }

  private createFromForm(): IModem {
    return {
      ...new Modem(),
      id: this.editForm.get(['id'])!.value,
      crmEstacaoId: this.editForm.get(['crmEstacaoId'])!.value,
      vsatId: this.editForm.get(['vsatId'])!.value,
      vsatUid: this.editForm.get(['vsatUid'])!.value,
      vsatGroup: this.editForm.get(['vsatGroup'])!.value,
      hub: this.editForm.get(['hub'])!.value,
      name: this.editForm.get(['name'])!.value,
      ipAddress: this.editForm.get(['ipAddress'])!.value,
      status: this.editForm.get(['status'])!.value,
      statusDesc: this.editForm.get(['statusDesc'])!.value,
      objectOID: this.editForm.get(['objectOID'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      lanIpAddress: this.editForm.get(['lanIpAddress'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IModem>>): void {
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
