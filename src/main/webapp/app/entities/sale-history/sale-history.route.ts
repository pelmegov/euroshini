import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SaleHistoryComponent } from './sale-history.component';
import { SaleHistoryDetailComponent } from './sale-history-detail.component';
import { SaleHistoryPopupComponent } from './sale-history-dialog.component';
import { SaleHistoryDeletePopupComponent } from './sale-history-delete-dialog.component';

@Injectable()
export class SaleHistoryResolvePagingParams implements Resolve<any> {

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

export const saleHistoryRoute: Routes = [
    {
        path: 'sale-history',
        component: SaleHistoryComponent,
        resolve: {
            'pagingParams': SaleHistoryResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleHistories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sale-history/:id',
        component: SaleHistoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleHistories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const saleHistoryPopupRoute: Routes = [
    {
        path: 'sale-history-new',
        component: SaleHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sale-history/:id/edit',
        component: SaleHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sale-history/:id/delete',
        component: SaleHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
