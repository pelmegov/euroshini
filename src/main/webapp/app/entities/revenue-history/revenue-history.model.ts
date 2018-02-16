import { BaseEntity } from './../../shared';
import { Tire } from '../tire';

export class RevenueHistory implements BaseEntity {

    public createdBy?: string;
    public createdDate?: Date;

    constructor(
        public id?: number,
        public count?: number,
        public tire?: Tire,
        createdBy?: string,
        createdDate?: Date,
    ) {
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
    }
}
