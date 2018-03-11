import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Tire } from './tire.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TireService {

    private resourceUrl =  SERVER_API_URL + 'api/tires';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/tires';

    constructor(private http: Http) { }

    create(tire: Tire): Observable<Tire> {
        const copy = this.convert(tire);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(tire: Tire): Observable<Tire> {
        const copy = this.convert(tire);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Tire> {
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

    getTireCount(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(`${this.resourceUrl}/count`, options)
            .map((res: Response) => this.simpleConvertResponse(res));
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

    private simpleConvertResponse(res: Response) {
        return new ResponseWrapper(res.headers, res.json(), res.status);
    }

    /**
     * Convert a returned JSON object to Tire.
     */
    private convertItemFromServer(json: any): Tire {
        const entity: Tire = Object.assign(new Tire(), json);
        return entity;
    }

    /**
     * Convert a Tire to a JSON which can be sent to the server.
     */
    private convert(tire: Tire): Tire {
        const copy: Tire = Object.assign({}, tire);
        return copy;
    }
}
