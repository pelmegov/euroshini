import { Route } from '@angular/router';

import { ReportsComponent } from './reports.component';

export const reportsRoute: Route = {
    path: 'reports',
    component: ReportsComponent,
    data: {
        pageTitle: 'Reports'
    }
};
