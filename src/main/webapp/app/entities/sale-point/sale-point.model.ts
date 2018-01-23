import { BaseEntity } from './../../shared';

export class SalePoint implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public tires?: BaseEntity[],
    ) {
    }
}
