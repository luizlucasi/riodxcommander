import { element, by, ElementFinder } from 'protractor';

export class ModemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-modem div table .btn-danger'));
  title = element.all(by.css('jhi-modem div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class ModemUpdatePage {
  pageTitle = element(by.id('jhi-modem-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  crmEstacaoIdInput = element(by.id('field_crmEstacaoId'));
  vsatIdInput = element(by.id('field_vsatId'));
  vsatUidInput = element(by.id('field_vsatUid'));
  vsatGroupInput = element(by.id('field_vsatGroup'));
  hubInput = element(by.id('field_hub'));
  nameInput = element(by.id('field_name'));
  ipAddressInput = element(by.id('field_ipAddress'));
  statusInput = element(by.id('field_status'));
  statusDescInput = element(by.id('field_statusDesc'));
  objectOIDInput = element(by.id('field_objectOID'));
  latitudeInput = element(by.id('field_latitude'));
  longitudeInput = element(by.id('field_longitude'));
  lanIpAddressInput = element(by.id('field_lanIpAddress'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCrmEstacaoIdInput(crmEstacaoId: string): Promise<void> {
    await this.crmEstacaoIdInput.sendKeys(crmEstacaoId);
  }

  async getCrmEstacaoIdInput(): Promise<string> {
    return await this.crmEstacaoIdInput.getAttribute('value');
  }

  async setVsatIdInput(vsatId: string): Promise<void> {
    await this.vsatIdInput.sendKeys(vsatId);
  }

  async getVsatIdInput(): Promise<string> {
    return await this.vsatIdInput.getAttribute('value');
  }

  async setVsatUidInput(vsatUid: string): Promise<void> {
    await this.vsatUidInput.sendKeys(vsatUid);
  }

  async getVsatUidInput(): Promise<string> {
    return await this.vsatUidInput.getAttribute('value');
  }

  async setVsatGroupInput(vsatGroup: string): Promise<void> {
    await this.vsatGroupInput.sendKeys(vsatGroup);
  }

  async getVsatGroupInput(): Promise<string> {
    return await this.vsatGroupInput.getAttribute('value');
  }

  async setHubInput(hub: string): Promise<void> {
    await this.hubInput.sendKeys(hub);
  }

  async getHubInput(): Promise<string> {
    return await this.hubInput.getAttribute('value');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setIpAddressInput(ipAddress: string): Promise<void> {
    await this.ipAddressInput.sendKeys(ipAddress);
  }

  async getIpAddressInput(): Promise<string> {
    return await this.ipAddressInput.getAttribute('value');
  }

  async setStatusInput(status: string): Promise<void> {
    await this.statusInput.sendKeys(status);
  }

  async getStatusInput(): Promise<string> {
    return await this.statusInput.getAttribute('value');
  }

  async setStatusDescInput(statusDesc: string): Promise<void> {
    await this.statusDescInput.sendKeys(statusDesc);
  }

  async getStatusDescInput(): Promise<string> {
    return await this.statusDescInput.getAttribute('value');
  }

  async setObjectOIDInput(objectOID: string): Promise<void> {
    await this.objectOIDInput.sendKeys(objectOID);
  }

  async getObjectOIDInput(): Promise<string> {
    return await this.objectOIDInput.getAttribute('value');
  }

  async setLatitudeInput(latitude: string): Promise<void> {
    await this.latitudeInput.sendKeys(latitude);
  }

  async getLatitudeInput(): Promise<string> {
    return await this.latitudeInput.getAttribute('value');
  }

  async setLongitudeInput(longitude: string): Promise<void> {
    await this.longitudeInput.sendKeys(longitude);
  }

  async getLongitudeInput(): Promise<string> {
    return await this.longitudeInput.getAttribute('value');
  }

  async setLanIpAddressInput(lanIpAddress: string): Promise<void> {
    await this.lanIpAddressInput.sendKeys(lanIpAddress);
  }

  async getLanIpAddressInput(): Promise<string> {
    return await this.lanIpAddressInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ModemDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-modem-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-modem'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
