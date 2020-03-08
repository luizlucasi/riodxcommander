import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BandComponentsPage, BandDeleteDialog, BandUpdatePage } from './band.page-object';

const expect = chai.expect;

describe('Band e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let bandComponentsPage: BandComponentsPage;
  let bandUpdatePage: BandUpdatePage;
  let bandDeleteDialog: BandDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Bands', async () => {
    await navBarPage.goToEntity('band');
    bandComponentsPage = new BandComponentsPage();
    await browser.wait(ec.visibilityOf(bandComponentsPage.title), 5000);
    expect(await bandComponentsPage.getTitle()).to.eq('libraryApp.band.home.title');
    await browser.wait(ec.or(ec.visibilityOf(bandComponentsPage.entities), ec.visibilityOf(bandComponentsPage.noResult)), 1000);
  });

  it('should load create Band page', async () => {
    await bandComponentsPage.clickOnCreateButton();
    bandUpdatePage = new BandUpdatePage();
    expect(await bandUpdatePage.getPageTitle()).to.eq('libraryApp.band.home.createOrEditLabel');
    await bandUpdatePage.cancel();
  });

  it('should create and save Bands', async () => {
    const nbButtonsBeforeCreate = await bandComponentsPage.countDeleteButtons();

    await bandComponentsPage.clickOnCreateButton();

    await promise.all([
      bandUpdatePage.setNameInput('name'),
      bandUpdatePage.bandMeterSelectLastOption(),
      bandUpdatePage.setCommandRunningBandInput('commandRunningBand')
    ]);

    expect(await bandUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    const selectedInUse = bandUpdatePage.getInUseInput();
    if (await selectedInUse.isSelected()) {
      await bandUpdatePage.getInUseInput().click();
      expect(await bandUpdatePage.getInUseInput().isSelected(), 'Expected inUse not to be selected').to.be.false;
    } else {
      await bandUpdatePage.getInUseInput().click();
      expect(await bandUpdatePage.getInUseInput().isSelected(), 'Expected inUse to be selected').to.be.true;
    }
    expect(await bandUpdatePage.getCommandRunningBandInput()).to.eq(
      'commandRunningBand',
      'Expected CommandRunningBand value to be equals to commandRunningBand'
    );

    await bandUpdatePage.save();
    expect(await bandUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await bandComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Band', async () => {
    const nbButtonsBeforeDelete = await bandComponentsPage.countDeleteButtons();
    await bandComponentsPage.clickOnLastDeleteButton();

    bandDeleteDialog = new BandDeleteDialog();
    expect(await bandDeleteDialog.getDialogTitle()).to.eq('libraryApp.band.delete.question');
    await bandDeleteDialog.clickOnConfirmButton();

    expect(await bandComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
