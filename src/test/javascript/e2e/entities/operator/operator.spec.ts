import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { OperatorComponentsPage, OperatorDeleteDialog, OperatorUpdatePage } from './operator.page-object';

const expect = chai.expect;

describe('Operator e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let operatorComponentsPage: OperatorComponentsPage;
  let operatorUpdatePage: OperatorUpdatePage;
  let operatorDeleteDialog: OperatorDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Operators', async () => {
    await navBarPage.goToEntity('operator');
    operatorComponentsPage = new OperatorComponentsPage();
    await browser.wait(ec.visibilityOf(operatorComponentsPage.title), 5000);
    expect(await operatorComponentsPage.getTitle()).to.eq('libraryApp.operator.home.title');
    await browser.wait(ec.or(ec.visibilityOf(operatorComponentsPage.entities), ec.visibilityOf(operatorComponentsPage.noResult)), 1000);
  });

  it('should load create Operator page', async () => {
    await operatorComponentsPage.clickOnCreateButton();
    operatorUpdatePage = new OperatorUpdatePage();
    expect(await operatorUpdatePage.getPageTitle()).to.eq('libraryApp.operator.home.createOrEditLabel');
    await operatorUpdatePage.cancel();
  });

  it('should create and save Operators', async () => {
    const nbButtonsBeforeCreate = await operatorComponentsPage.countDeleteButtons();

    await operatorComponentsPage.clickOnCreateButton();

    await promise.all([operatorUpdatePage.setCallInput('call')]);

    expect(await operatorUpdatePage.getCallInput()).to.eq('call', 'Expected Call value to be equals to call');

    await operatorUpdatePage.save();
    expect(await operatorUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await operatorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Operator', async () => {
    const nbButtonsBeforeDelete = await operatorComponentsPage.countDeleteButtons();
    await operatorComponentsPage.clickOnLastDeleteButton();

    operatorDeleteDialog = new OperatorDeleteDialog();
    expect(await operatorDeleteDialog.getDialogTitle()).to.eq('libraryApp.operator.delete.question');
    await operatorDeleteDialog.clickOnConfirmButton();

    expect(await operatorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
