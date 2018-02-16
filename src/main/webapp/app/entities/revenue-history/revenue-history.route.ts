import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { RevenueHistoryComponent } from './revenue-history.component';
import { RevenueHistoryDetailComponent } from './revenue-history-detail.component';
import { RevenueHistoryPopupComponent } from './revenue-history-dialog.component';
import { RevenueHistoryDeletePopupComponent } from './revenue-history-delete-dialog.component';

@Injectable()
export class RevenueHistoryResolvePagingParams implements Resolve<any> {

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

export const revenueHistoryRoute: Routes = [
    {
        path: 'revenue-history',
        component: RevenueHistoryComponent,
        resolve: {
            'pagingParams': RevenueHistoryResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RevenueHistories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'revenue-history/:id',
        component: RevenueHistoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RevenueHistories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const revenueHistoryPopupRoute: Routes = [
    {
        path: 'revenue-history-new',
        component: RevenueHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RevenueHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'revenue-history/:id/edit',
        component: RevenueHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RevenueHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'revenue-history/:id/delete',
        component: RevenueHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RevenueHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
