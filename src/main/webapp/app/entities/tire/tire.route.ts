import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TireComponent } from './tire.component';
import { TireDetailComponent } from './tire-detail.component';
import { TirePopupComponent } from './tire-dialog.component';
import { TireDeletePopupComponent } from './tire-delete-dialog.component';

@Injectable()
export class TireResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const tireRoute: Routes = [
    {
        path: 'tire',
        component: TireComponent,
        resolve: {
            'pagingParams': TireResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tires'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tire/:id',
        component: TireDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tires'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tirePopupRoute: Routes = [
    {
        path: 'tire-new',
        component: TirePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tires'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tire/:id/edit',
        component: TirePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tires'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tire/:id/delete',
        component: TireDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tires'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
