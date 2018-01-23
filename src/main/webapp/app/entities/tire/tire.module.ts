import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EuroshiniSharedModule } from '../../shared';
import {
    TireService,
    TirePopupService,
    TireComponent,
    TireDetailComponent,
    TireDialogComponent,
    TirePopupComponent,
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
        TireDeleteDialogComponent,
        TirePopupComponent,
        TireDeletePopupComponent,
    ],
    entryComponents: [
        TireComponent,
        TireDialogComponent,
        TirePopupComponent,
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
