import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LibrarySharedModule } from 'app/shared/shared.module';
import { ModemComponent } from './modem.component';
import { ModemDetailComponent } from './modem-detail.component';
import { ModemUpdateComponent } from './modem-update.component';
import { ModemDeleteDialogComponent } from './modem-delete-dialog.component';
import { modemRoute } from './modem.route';

@NgModule({
  imports: [LibrarySharedModule, RouterModule.forChild(modemRoute)],
  declarations: [ModemComponent, ModemDetailComponent, ModemUpdateComponent, ModemDeleteDialogComponent],
  entryComponents: [ModemDeleteDialogComponent]
})
export class LibraryModemModule {}
