import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { SaleHistory } from './sale-history.model';
import { SaleHistoryService } from './sale-history.service';

@Component({
    selector: 'jhi-sale-history-detail',
    templateUrl: './sale-history-detail.component.html'
})
export class SaleHistoryDetailComponent implements OnInit, OnDestroy {

    saleHistory: SaleHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private saleHistoryService: SaleHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSaleHistories();
    }

    load(id) {
        this.saleHistoryService.find(id).subscribe((saleHistory) => {
            this.saleHistory = saleHistory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSaleHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'saleHistoryListModification',
            (response) => this.load(this.saleHistory.id)
        );
    }
}
