import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EuroshiniSharedModule } from '../../shared';
import {
    RevenueHistoryService,
    RevenueHistoryPopupService,
    RevenueHistoryComponent,
    RevenueHistoryDetailComponent,
    RevenueHistoryDialogComponent,
    RevenueHistoryPopupComponent,
    RevenueHistoryDeletePopupComponent,
    RevenueHistoryDeleteDialogComponent,
    revenueHistoryRoute,
    revenueHistoryPopupRoute,
    RevenueHistoryResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...revenueHistoryRoute,
    ...revenueHistoryPopupRoute,
];

@NgModule({
    imports: [
        EuroshiniSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RevenueHistoryComponent,
        RevenueHistoryDetailComponent,
        RevenueHistoryDialogComponent,
        RevenueHistoryDeleteDialogComponent,
        RevenueHistoryPopupComponent,
        RevenueHistoryDeletePopupComponent,
    ],
    entryComponents: [
        RevenueHistoryComponent,
        RevenueHistoryDialogComponent,
        RevenueHistoryPopupComponent,
        RevenueHistoryDeleteDialogComponent,
        RevenueHistoryDeletePopupComponent,
    ],
    providers: [
        RevenueHistoryService,
        RevenueHistoryPopupService,
        RevenueHistoryResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EuroshiniRevenueHistoryModule {}
