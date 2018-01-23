import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { SalePoint } from './sale-point.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SalePointService {

    private resourceUrl =  SERVER_API_URL + 'api/sale-points';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/sale-points';

    constructor(private http: Http) { }

    create(salePoint: SalePoint): Observable<SalePoint> {
        const copy = this.convert(salePoint);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(salePoint: SalePoint): Observable<SalePoint> {
        const copy = this.convert(salePoint);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<SalePoint> {
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
     * Convert a returned JSON object to SalePoint.
     */
    private convertItemFromServer(json: any): SalePoint {
        const entity: SalePoint = Object.assign(new SalePoint(), json);
        return entity;
    }

    /**
     * Convert a SalePoint to a JSON which can be sent to the server.
     */
    private convert(salePoint: SalePoint): SalePoint {
        const copy: SalePoint = Object.assign({}, salePoint);
        return copy;
    }
}
