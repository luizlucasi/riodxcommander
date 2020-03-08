import { element, by, ElementFinder } from 'protractor';

export class BandComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-band div table .btn-danger'));
  title = element.all(by.css('jhi-band div h2#page-heading span')).first();
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

export class BandUpdatePage {
  pageTitle = element(by.id('jhi-band-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nameInput = element(by.id('field_name'));
  bandMeterSelect = element(by.id('field_bandMeter'));
  inUseInput = element(by.id('field_inUse'));
  commandRunningBandInput = element(by.id('field_commandRunningBand'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setBandMeterSelect(bandMeter: string): Promise<void> {
    await this.bandMeterSelect.sendKeys(bandMeter);
  }

  async getBandMeterSelect(): Promise<string> {
    return await this.bandMeterSelect.element(by.css('option:checked')).getText();
  }

  async bandMeterSelectLastOption(): Promise<void> {
    await this.bandMeterSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  getInUseInput(): ElementFinder {
    return this.inUseInput;
  }

  async setCommandRunningBandInput(commandRunningBand: string): Promise<void> {
    await this.commandRunningBandInput.sendKeys(commandRunningBand);
  }

  async getCommandRunningBandInput(): Promise<string> {
    return await this.commandRunningBandInput.getAttribute('value');
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

export class BandDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-band-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-band'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
