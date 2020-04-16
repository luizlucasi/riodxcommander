import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IModem, Modem } from 'app/shared/model/modem.model';
import { ModemService } from './modem.service';
import { ModemComponent } from './modem.component';
import { ModemDetailComponent } from './modem-detail.component';
import { ModemUpdateComponent } from './modem-update.component';

@Injectable({ providedIn: 'root' })
export class ModemResolve implements Resolve<IModem> {
  constructor(private service: ModemService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IModem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((modem: HttpResponse<Modem>) => {
          if (modem.body) {
            return of(modem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Modem());
  }
}

export const modemRoute: Routes = [
  {
    path: '',
    component: ModemComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'libraryApp.modem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ModemDetailComponent,
    resolve: {
      modem: ModemResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'libraryApp.modem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ModemUpdateComponent,
    resolve: {
      modem: ModemResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'libraryApp.modem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ModemUpdateComponent,
    resolve: {
      modem: ModemResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'libraryApp.modem.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
