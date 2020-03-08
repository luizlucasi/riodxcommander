import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LibrarySharedModule } from 'app/shared/shared.module';
import { BandComponent } from './band.component';
import { BandDetailComponent } from './band-detail.component';
import { BandUpdateComponent } from './band-update.component';
import { BandDeleteDialogComponent } from './band-delete-dialog.component';
import { bandRoute } from './band.route';

@NgModule({
  imports: [LibrarySharedModule, RouterModule.forChild(bandRoute)],
  declarations: [BandComponent, BandDetailComponent, BandUpdateComponent, BandDeleteDialogComponent],
  entryComponents: [BandDeleteDialogComponent]
})
export class LibraryBandModule {}
