import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

@Injectable()
export class ReportsService  {
    constructor(private http: Http) { }

    getRevenueHistories(req: any): Observable<Response> {
        const params: URLSearchParams = new URLSearchParams();
        params.set('fromDate', req.fromDate);
        params.set('toDate', req.toDate);

        const options = {
            search: params
        };

        return this.http.get(SERVER_API_URL + 'management/reports/revenue', options);
    }

    getSaleHistories(req: any): Observable<Response> {
        const params: URLSearchParams = new URLSearchParams();
        params.set('fromDate', req.fromDate);
        params.set('toDate', req.toDate);

        const options = {
            search: params
        };

        return this.http.get(SERVER_API_URL + 'management/reports/sale', options);
    }
}
