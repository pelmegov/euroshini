import { BaseEntity } from './../../shared';
import { Tire } from '../tire';

export class SaleHistory implements BaseEntity {

    public createdBy?: string;
    public createdDate?: Date;

    constructor(
        public id?: number,
        public count?: number,
        public price?: number,
        public sum?: number,
        public tire?: Tire,
        createdBy?: string,
        createdDate?: Date,
    ) {
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
    }
}
