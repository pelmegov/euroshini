import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SalePointComponent } from './sale-point.component';
import { SalePointDetailComponent } from './sale-point-detail.component';
import { SalePointPopupComponent } from './sale-point-dialog.component';
import { SalePointDeletePopupComponent } from './sale-point-delete-dialog.component';

export const salePointRoute: Routes = [
    {
        path: 'sale-point',
        component: SalePointComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SalePoints'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sale-point/:id',
        component: SalePointDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SalePoints'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const salePointPopupRoute: Routes = [
    {
        path: 'sale-point-new',
        component: SalePointPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SalePoints'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sale-point/:id/edit',
        component: SalePointPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SalePoints'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sale-point/:id/delete',
        component: SalePointDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SalePoints'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
