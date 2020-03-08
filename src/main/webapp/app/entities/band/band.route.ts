import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBand, Band } from 'app/shared/model/band.model';
import { BandService } from './band.service';
import { BandComponent } from './band.component';
import { BandDetailComponent } from './band-detail.component';
import { BandUpdateComponent } from './band-update.component';

@Injectable({ providedIn: 'root' })
export class BandResolve implements Resolve<IBand> {
  constructor(private service: BandService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBand> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((band: HttpResponse<Band>) => {
          if (band.body) {
            return of(band.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Band());
  }
}

export const bandRoute: Routes = [
  {
    path: '',
    component: BandComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'libraryApp.band.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BandDetailComponent,
    resolve: {
      band: BandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'libraryApp.band.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BandUpdateComponent,
    resolve: {
      band: BandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'libraryApp.band.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BandUpdateComponent,
    resolve: {
      band: BandResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'libraryApp.band.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
