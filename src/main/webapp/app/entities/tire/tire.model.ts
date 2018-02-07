import { BaseEntity } from './../../shared';
import { SalePoint } from '../sale-point';

export const enum Season {
    'WINTER',
    'SUMMER',
    'ALL_SEASON'
}

export const enum Manufacturer {
    'EURO',
    'CHINA'
}

export const enum Technology {
    'DEFAULT',
    'RUNFLAT'
}

export class Tire implements BaseEntity {
    constructor(
        public id?: number,
        public mark?: string,
        public model?: string,
        public radius?: number,
        public size?: string,
        public technology?: Technology,
        public index?: string,
        public releaseYear?: string,
        public isStrong?: boolean,
        public season?: Season,
        public manufacturer?: Manufacturer,
        public price?: number,
        public count?: number,
        public salePoint?: SalePoint,
    ) {
        this.isStrong = false;
    }
}
