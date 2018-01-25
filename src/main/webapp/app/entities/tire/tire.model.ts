import { BaseEntity } from './../../shared';

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
        public title?: string,
        public isStrong?: boolean,
        public radius?: number,
        public releaseYear?: string,
        public size?: string,
        public mark?: string,
        public model?: string,
        public index?: string,
        public price?: number,
        public count?: number,
        public season?: Season,
        public manufacturer?: Manufacturer,
        public technology?: Technology,
        public salePoint?: BaseEntity,
    ) {
        this.isStrong = false;
    }
}
