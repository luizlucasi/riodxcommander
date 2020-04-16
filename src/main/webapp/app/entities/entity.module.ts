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
      },
      {
        path: 'modem',
        loadChildren: () => import('./modem/modem.module').then(m => m.LibraryModemModule)
      },
      {
        path: 'nota-fiscal',
        loadChildren: () => import('./nota-fiscal/nota-fiscal.module').then(m => m.LibraryNotaFiscalModule)
      },
      {
        path: 'cliente',
        loadChildren: () => import('./cliente/cliente.module').then(m => m.LibraryClienteModule)
      },
      {
        path: 'contrato',
        loadChildren: () => import('./contrato/contrato.module').then(m => m.LibraryContratoModule)
      },
      {
        path: 'circuito',
        loadChildren: () => import('./circuito/circuito.module').then(m => m.LibraryCircuitoModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class LibraryEntityModule {}
