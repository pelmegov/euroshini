import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { EuroshiniTireModule } from './tire/tire.module';
import { EuroshiniSalePointModule } from './sale-point/sale-point.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        EuroshiniTireModule,
        EuroshiniSalePointModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EuroshiniEntityModule {}
