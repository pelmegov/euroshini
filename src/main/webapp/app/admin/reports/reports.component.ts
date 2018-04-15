import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { JhiParseLinks } from 'ng-jhipster';

import { ReportsService } from './reports.service';
import { ITEMS_PER_PAGE } from '../../shared';
import { RevenueHistory } from '../../entities/revenue-history';
import { SaleHistory } from '../../entities/sale-history';

@Component({
  selector: 'jhi-audit',
  templateUrl: './reports.component.html'
})
export class ReportsComponent implements OnInit {

    fromDate: string;
    toDate: string;
    datePipe: DatePipe;

    revenueHistories: RevenueHistory[];
    revenueTireCounts: number;
    isRevenueCollapsed = true;

    saleHistories: SaleHistory[];
    saleTireCounts: number;
    isSaleCollapsed = true;
    saleUsers: Map<string, number>;

    constructor(private reportsService: ReportsService) {
        this.datePipe = new DatePipe('en');
    }

    loadPage() {
        this.onChangeDate();
    }

    ngOnInit() {
        this.today();
        this.previousMonth();
        this.onChangeDate();
    }

    onChangeDate() {
        this.reportsService.getRevenueHistories({fromDate: this.fromDate, toDate: this.toDate}).subscribe((res) => {
            this.revenueHistories = res.json();
            this.revenueTireCounts = this.getTireCountByEntity(this.revenueHistories);
        });

        this.reportsService.getSaleHistories({fromDate: this.fromDate, toDate: this.toDate}).subscribe((res) => {
            this.saleHistories = res.json();
            this.saleTireCounts = this.getTireCountByEntity(this.saleHistories);
            this.putSaleUsers(this.saleHistories);
        });
    }

    private getTireCountByEntity(entity: any[]) {
        let col = 0;
        entity.forEach((item) => {
            col += item.count;
        });
        return col;
    }

    private putSaleUsers(saleHistories: SaleHistory[]) {
        this.saleUsers = new Map<string, number>();
        saleHistories.forEach((item) => {
            const userName = item.createdBy;
            if (this.saleUsers.get(userName) === undefined) {
                this.saleUsers.set(userName, 0);
            }
            this.saleUsers.set(userName, this.saleUsers.get(userName) + item.count);
        });
    }

    previousMonth() {
        const dateFormat = 'yyyy-MM-dd';
        let fromDate: Date = new Date();

        if (fromDate.getMonth() === 0) {
            fromDate = new Date(fromDate.getFullYear() - 1, 11, fromDate.getDate());
        } else {
            fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
        }

        this.fromDate = this.datePipe.transform(fromDate, dateFormat);
    }

    today() {
        const dateFormat = 'yyyy-MM-dd';
        // Today + 1 day - needed if the current day must be included
        const today: Date = new Date();
        today.setDate(today.getDate() + 1);
        const date = new Date(today.getFullYear(), today.getMonth(), today.getDate());
        this.toDate = this.datePipe.transform(date, dateFormat);
    }
}
