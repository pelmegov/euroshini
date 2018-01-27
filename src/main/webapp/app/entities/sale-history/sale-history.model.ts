import { BaseEntity } from './../../shared';

export class SaleHistory implements BaseEntity {

    public createdBy?: string;
    public createdDate?: Date;

    constructor(
        public id?: number,
        public count?: number,
        public price?: number,
        public sum?: number,
        public tire?: BaseEntity,
        createdBy?: string,
        createdDate?: Date,
    ) {
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
    }
}
