import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { EuroshiniTireModule } from './tire/tire.module';
import { EuroshiniSalePointModule } from './sale-point/sale-point.module';
import { EuroshiniSaleHistoryModule } from './sale-history/sale-history.module';
import { EuroshiniRevenueHistoryModule } from './revenue-history/revenue-history.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        EuroshiniTireModule,
        EuroshiniSalePointModule,
        EuroshiniSaleHistoryModule,
        EuroshiniRevenueHistoryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EuroshiniEntityModule {}
