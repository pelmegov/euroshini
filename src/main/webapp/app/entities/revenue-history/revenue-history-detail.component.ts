import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { RevenueHistory } from './revenue-history.model';
import { RevenueHistoryService } from './revenue-history.service';

@Component({
    selector: 'jhi-revenue-history-detail',
    templateUrl: './revenue-history-detail.component.html'
})
export class RevenueHistoryDetailComponent implements OnInit, OnDestroy {

    revenueHistory: RevenueHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private revenueHistoryService: RevenueHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRevenueHistories();
    }

    load(id) {
        this.revenueHistoryService.find(id).subscribe((revenueHistory) => {
            this.revenueHistory = revenueHistory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRevenueHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'revenueHistoryListModification',
            (response) => this.load(this.revenueHistory.id)
        );
    }
}
