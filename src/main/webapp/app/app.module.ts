import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ngx-webstorage';

import { EuroshiniSharedModule, UserRouteAccessService } from './shared';
import { EuroshiniAppRoutingModule} from './app-routing.module';
import { EuroshiniHomeModule } from './home/home.module';
import { EuroshiniAdminModule } from './admin/admin.module';
import { EuroshiniAccountModule } from './account/account.module';
import { EuroshiniEntityModule } from './entities/entity.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        EuroshiniAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        EuroshiniSharedModule,
        EuroshiniHomeModule,
        EuroshiniAdminModule,
        EuroshiniAccountModule,
        EuroshiniEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class EuroshiniAppModule {}
