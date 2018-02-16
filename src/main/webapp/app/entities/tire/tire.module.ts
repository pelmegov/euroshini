import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EuroshiniSharedModule } from '../../shared';
import {
    TireService,
    TirePopupService,
    TireComponent,
    TireDetailComponent,
    TireDialogComponent,
    TireSaleDialogComponent,
    TireRevenuePopupComponent,
    TireRevenueDialogComponent,
    TirePopupComponent,
    TireSalePopupComponent,
    TireDeletePopupComponent,
    TireDeleteDialogComponent,
    tireRoute,
    tirePopupRoute,
    TireResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...tireRoute,
    ...tirePopupRoute,
];

@NgModule({
    imports: [
        EuroshiniSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TireComponent,
        TireDetailComponent,
        TireDialogComponent,
        TireSaleDialogComponent,
        TireRevenueDialogComponent,
        TireDeleteDialogComponent,
        TirePopupComponent,
        TireSalePopupComponent,
        TireRevenuePopupComponent,
        TireDeletePopupComponent,
    ],
    entryComponents: [
        TireComponent,
        TireSaleDialogComponent,
        TireRevenueDialogComponent,
        TireDialogComponent,
        TirePopupComponent,
        TireSalePopupComponent,
        TireRevenuePopupComponent,
        TireDeleteDialogComponent,
        TireDeletePopupComponent,
    ],
    providers: [
        TireService,
        TirePopupService,
        TireResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EuroshiniTireModule {}
