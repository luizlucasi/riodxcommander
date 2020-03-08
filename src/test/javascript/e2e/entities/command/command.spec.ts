import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CommandComponentsPage, CommandDeleteDialog, CommandUpdatePage } from './command.page-object';

const expect = chai.expect;

describe('Command e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let commandComponentsPage: CommandComponentsPage;
  let commandUpdatePage: CommandUpdatePage;
  let commandDeleteDialog: CommandDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Commands', async () => {
    await navBarPage.goToEntity('command');
    commandComponentsPage = new CommandComponentsPage();
    await browser.wait(ec.visibilityOf(commandComponentsPage.title), 5000);
    expect(await commandComponentsPage.getTitle()).to.eq('libraryApp.command.home.title');
    await browser.wait(ec.or(ec.visibilityOf(commandComponentsPage.entities), ec.visibilityOf(commandComponentsPage.noResult)), 1000);
  });

  it('should load create Command page', async () => {
    await commandComponentsPage.clickOnCreateButton();
    commandUpdatePage = new CommandUpdatePage();
    expect(await commandUpdatePage.getPageTitle()).to.eq('libraryApp.command.home.createOrEditLabel');
    await commandUpdatePage.cancel();
  });

  it('should create and save Commands', async () => {
    const nbButtonsBeforeCreate = await commandComponentsPage.countDeleteButtons();

    await commandComponentsPage.clickOnCreateButton();

    await promise.all([
      commandUpdatePage.setCallInput('call'),
      commandUpdatePage.operatorSelectLastOption()
      // commandUpdatePage.bandSelectLastOption(),
    ]);

    expect(await commandUpdatePage.getCallInput()).to.eq('call', 'Expected Call value to be equals to call');
    const selectedInUse = commandUpdatePage.getInUseInput();
    if (await selectedInUse.isSelected()) {
      await commandUpdatePage.getInUseInput().click();
      expect(await commandUpdatePage.getInUseInput().isSelected(), 'Expected inUse not to be selected').to.be.false;
    } else {
      await commandUpdatePage.getInUseInput().click();
      expect(await commandUpdatePage.getInUseInput().isSelected(), 'Expected inUse to be selected').to.be.true;
    }

    await commandUpdatePage.save();
    expect(await commandUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await commandComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Command', async () => {
    const nbButtonsBeforeDelete = await commandComponentsPage.countDeleteButtons();
    await commandComponentsPage.clickOnLastDeleteButton();

    commandDeleteDialog = new CommandDeleteDialog();
    expect(await commandDeleteDialog.getDialogTitle()).to.eq('libraryApp.command.delete.question');
    await commandDeleteDialog.clickOnConfirmButton();

    expect(await commandComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
