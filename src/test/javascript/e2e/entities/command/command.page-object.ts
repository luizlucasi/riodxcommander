import { element, by, ElementFinder } from 'protractor';

export class CommandComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-command div table .btn-danger'));
  title = element.all(by.css('jhi-command div h2#page-heading span')).first();
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

export class CommandUpdatePage {
  pageTitle = element(by.id('jhi-command-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  callInput = element(by.id('field_call'));
  inUseInput = element(by.id('field_inUse'));

  operatorSelect = element(by.id('field_operator'));
  bandSelect = element(by.id('field_band'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCallInput(call: string): Promise<void> {
    await this.callInput.sendKeys(call);
  }

  async getCallInput(): Promise<string> {
    return await this.callInput.getAttribute('value');
  }

  getInUseInput(): ElementFinder {
    return this.inUseInput;
  }

  async operatorSelectLastOption(): Promise<void> {
    await this.operatorSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async operatorSelectOption(option: string): Promise<void> {
    await this.operatorSelect.sendKeys(option);
  }

  getOperatorSelect(): ElementFinder {
    return this.operatorSelect;
  }

  async getOperatorSelectedOption(): Promise<string> {
    return await this.operatorSelect.element(by.css('option:checked')).getText();
  }

  async bandSelectLastOption(): Promise<void> {
    await this.bandSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async bandSelectOption(option: string): Promise<void> {
    await this.bandSelect.sendKeys(option);
  }

  getBandSelect(): ElementFinder {
    return this.bandSelect;
  }

  async getBandSelectedOption(): Promise<string> {
    return await this.bandSelect.element(by.css('option:checked')).getText();
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

export class CommandDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-command-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-command'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
