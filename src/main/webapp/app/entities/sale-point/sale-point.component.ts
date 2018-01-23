import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SalePoint } from './sale-point.model';
import { SalePointService } from './sale-point.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sale-point',
    templateUrl: './sale-point.component.html'
})
export class SalePointComponent implements OnInit, OnDestroy {
salePoints: SalePoint[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private salePointService: SalePointService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.salePointService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.salePoints = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.salePointService.query().subscribe(
            (res: ResponseWrapper) => {
                this.salePoints = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSalePoints();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SalePoint) {
        return item.id;
    }
    registerChangeInSalePoints() {
        this.eventSubscriber = this.eventManager.subscribe('salePointListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
