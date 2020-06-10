import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRadio, Radio } from 'app/shared/model/radio.model';
import { RadioService } from './radio.service';
import { RadioComponent } from './radio.component';
import { RadioDetailComponent } from './radio-detail.component';
import { RadioUpdateComponent } from './radio-update.component';

@Injectable({ providedIn: 'root' })
export class RadioResolve implements Resolve<IRadio> {
  constructor(private service: RadioService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRadio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((radio: HttpResponse<Radio>) => {
          if (radio.body) {
            return of(radio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Radio());
  }
}

export const radioRoute: Routes = [
  {
    path: '',
    component: RadioComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'libraryApp.radio.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RadioDetailComponent,
    resolve: {
      radio: RadioResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'libraryApp.radio.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RadioUpdateComponent,
    resolve: {
      radio: RadioResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'libraryApp.radio.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RadioUpdateComponent,
    resolve: {
      radio: RadioResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'libraryApp.radio.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
