import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ModemComponentsPage, ModemDeleteDialog, ModemUpdatePage } from './modem.page-object';

const expect = chai.expect;

describe('Modem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let modemComponentsPage: ModemComponentsPage;
  let modemUpdatePage: ModemUpdatePage;
  let modemDeleteDialog: ModemDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Modems', async () => {
    await navBarPage.goToEntity('modem');
    modemComponentsPage = new ModemComponentsPage();
    await browser.wait(ec.visibilityOf(modemComponentsPage.title), 5000);
    expect(await modemComponentsPage.getTitle()).to.eq('libraryApp.modem.home.title');
    await browser.wait(ec.or(ec.visibilityOf(modemComponentsPage.entities), ec.visibilityOf(modemComponentsPage.noResult)), 1000);
  });

  it('should load create Modem page', async () => {
    await modemComponentsPage.clickOnCreateButton();
    modemUpdatePage = new ModemUpdatePage();
    expect(await modemUpdatePage.getPageTitle()).to.eq('libraryApp.modem.home.createOrEditLabel');
    await modemUpdatePage.cancel();
  });

  it('should create and save Modems', async () => {
    const nbButtonsBeforeCreate = await modemComponentsPage.countDeleteButtons();

    await modemComponentsPage.clickOnCreateButton();

    await promise.all([
      modemUpdatePage.setCrmEstacaoIdInput('5'),
      modemUpdatePage.setVsatIdInput('5'),
      modemUpdatePage.setVsatUidInput('5'),
      modemUpdatePage.setVsatGroupInput('vsatGroup'),
      modemUpdatePage.setHubInput('5'),
      modemUpdatePage.setNameInput('name'),
      modemUpdatePage.setIpAddressInput('ipAddress'),
      modemUpdatePage.setStatusInput('5'),
      modemUpdatePage.setStatusDescInput('statusDesc'),
      modemUpdatePage.setObjectOIDInput('objectOID'),
      modemUpdatePage.setLatitudeInput('5'),
      modemUpdatePage.setLongitudeInput('5'),
      modemUpdatePage.setLanIpAddressInput('lanIpAddress')
    ]);

    expect(await modemUpdatePage.getCrmEstacaoIdInput()).to.eq('5', 'Expected crmEstacaoId value to be equals to 5');
    expect(await modemUpdatePage.getVsatIdInput()).to.eq('5', 'Expected vsatId value to be equals to 5');
    expect(await modemUpdatePage.getVsatUidInput()).to.eq('5', 'Expected vsatUid value to be equals to 5');
    expect(await modemUpdatePage.getVsatGroupInput()).to.eq('vsatGroup', 'Expected VsatGroup value to be equals to vsatGroup');
    expect(await modemUpdatePage.getHubInput()).to.eq('5', 'Expected hub value to be equals to 5');
    expect(await modemUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await modemUpdatePage.getIpAddressInput()).to.eq('ipAddress', 'Expected IpAddress value to be equals to ipAddress');
    expect(await modemUpdatePage.getStatusInput()).to.eq('5', 'Expected status value to be equals to 5');
    expect(await modemUpdatePage.getStatusDescInput()).to.eq('statusDesc', 'Expected StatusDesc value to be equals to statusDesc');
    expect(await modemUpdatePage.getObjectOIDInput()).to.eq('objectOID', 'Expected ObjectOID value to be equals to objectOID');
    expect(await modemUpdatePage.getLatitudeInput()).to.eq('5', 'Expected latitude value to be equals to 5');
    expect(await modemUpdatePage.getLongitudeInput()).to.eq('5', 'Expected longitude value to be equals to 5');
    expect(await modemUpdatePage.getLanIpAddressInput()).to.eq('lanIpAddress', 'Expected LanIpAddress value to be equals to lanIpAddress');

    await modemUpdatePage.save();
    expect(await modemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await modemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Modem', async () => {
    const nbButtonsBeforeDelete = await modemComponentsPage.countDeleteButtons();
    await modemComponentsPage.clickOnLastDeleteButton();

    modemDeleteDialog = new ModemDeleteDialog();
    expect(await modemDeleteDialog.getDialogTitle()).to.eq('libraryApp.modem.delete.question');
    await modemDeleteDialog.clickOnConfirmButton();

    expect(await modemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
