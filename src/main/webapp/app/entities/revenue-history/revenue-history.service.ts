import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { RevenueHistory } from './revenue-history.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RevenueHistoryService {

    private resourceUrl =  SERVER_API_URL + 'api/revenue-histories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/revenue-histories';

    constructor(private http: Http) { }

    create(revenueHistory: RevenueHistory): Observable<RevenueHistory> {
        const copy = this.convert(revenueHistory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(revenueHistory: RevenueHistory): Observable<RevenueHistory> {
        const copy = this.convert(revenueHistory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<RevenueHistory> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to RevenueHistory.
     */
    private convertItemFromServer(json: any): RevenueHistory {
        const entity: RevenueHistory = Object.assign(new RevenueHistory(), json);
        return entity;
    }

    /**
     * Convert a RevenueHistory to a JSON which can be sent to the server.
     */
    private convert(revenueHistory: RevenueHistory): RevenueHistory {
        const copy: RevenueHistory = Object.assign({}, revenueHistory);
        return copy;
    }
}
