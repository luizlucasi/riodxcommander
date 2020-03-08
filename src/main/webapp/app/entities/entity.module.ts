import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'operator',
        loadChildren: () => import('./operator/operator.module').then(m => m.LibraryOperatorModule)
      },
      {
        path: 'command',
        loadChildren: () => import('./command/command.module').then(m => m.LibraryCommandModule)
      },
      {
        path: 'band',
        loadChildren: () => import('./band/band.module').then(m => m.LibraryBandModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class LibraryEntityModule {}
