import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { RadioComponentsPage, RadioDeleteDialog, RadioUpdatePage } from './radio.page-object';

const expect = chai.expect;

describe('Radio e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let radioComponentsPage: RadioComponentsPage;
  let radioUpdatePage: RadioUpdatePage;
  let radioDeleteDialog: RadioDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Radios', async () => {
    await navBarPage.goToEntity('radio');
    radioComponentsPage = new RadioComponentsPage();
    await browser.wait(ec.visibilityOf(radioComponentsPage.title), 5000);
    expect(await radioComponentsPage.getTitle()).to.eq('libraryApp.radio.home.title');
    await browser.wait(ec.or(ec.visibilityOf(radioComponentsPage.entities), ec.visibilityOf(radioComponentsPage.noResult)), 1000);
  });

  it('should load create Radio page', async () => {
    await radioComponentsPage.clickOnCreateButton();
    radioUpdatePage = new RadioUpdatePage();
    expect(await radioUpdatePage.getPageTitle()).to.eq('libraryApp.radio.home.createOrEditLabel');
    await radioUpdatePage.cancel();
  });

  it('should create and save Radios', async () => {
    const nbButtonsBeforeCreate = await radioComponentsPage.countDeleteButtons();

    await radioComponentsPage.clickOnCreateButton();

    await promise.all([radioUpdatePage.setDescriptionInput('description')]);

    expect(await radioUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');

    await radioUpdatePage.save();
    expect(await radioUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await radioComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Radio', async () => {
    const nbButtonsBeforeDelete = await radioComponentsPage.countDeleteButtons();
    await radioComponentsPage.clickOnLastDeleteButton();

    radioDeleteDialog = new RadioDeleteDialog();
    expect(await radioDeleteDialog.getDialogTitle()).to.eq('libraryApp.radio.delete.question');
    await radioDeleteDialog.clickOnConfirmButton();

    expect(await radioComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
