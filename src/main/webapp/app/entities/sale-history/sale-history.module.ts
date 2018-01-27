import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EuroshiniSharedModule } from '../../shared';
import {
    SaleHistoryService,
    SaleHistoryPopupService,
    SaleHistoryComponent,
    SaleHistoryDetailComponent,
    SaleHistoryDialogComponent,
    SaleHistoryPopupComponent,
    SaleHistoryDeletePopupComponent,
    SaleHistoryDeleteDialogComponent,
    saleHistoryRoute,
    saleHistoryPopupRoute,
    SaleHistoryResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...saleHistoryRoute,
    ...saleHistoryPopupRoute,
];

@NgModule({
    imports: [
        EuroshiniSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SaleHistoryComponent,
        SaleHistoryDetailComponent,
        SaleHistoryDialogComponent,
        SaleHistoryDeleteDialogComponent,
        SaleHistoryPopupComponent,
        SaleHistoryDeletePopupComponent,
    ],
    entryComponents: [
        SaleHistoryComponent,
        SaleHistoryDialogComponent,
        SaleHistoryPopupComponent,
        SaleHistoryDeleteDialogComponent,
        SaleHistoryDeletePopupComponent,
    ],
    providers: [
        SaleHistoryService,
        SaleHistoryPopupService,
        SaleHistoryResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EuroshiniSaleHistoryModule {}
