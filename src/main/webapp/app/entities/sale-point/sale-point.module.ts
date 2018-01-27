import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EuroshiniSharedModule } from '../../shared';
import {
    SalePointService,
    SalePointPopupService,
    SalePointComponent,
    SalePointDetailComponent,
    SalePointDialogComponent,
    SalePointPopupComponent,
    salePointRoute,
    salePointPopupRoute,
} from './';

const ENTITY_STATES = [
    ...salePointRoute,
    ...salePointPopupRoute,
];

@NgModule({
    imports: [
        EuroshiniSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SalePointComponent,
        SalePointDetailComponent,
        SalePointDialogComponent,
        SalePointPopupComponent,
    ],
    entryComponents: [
        SalePointComponent,
        SalePointDialogComponent,
        SalePointPopupComponent,
    ],
    providers: [
        SalePointService,
        SalePointPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EuroshiniSalePointModule {}
