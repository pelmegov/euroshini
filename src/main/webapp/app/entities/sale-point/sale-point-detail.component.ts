import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { SalePoint } from './sale-point.model';
import { SalePointService } from './sale-point.service';

@Component({
    selector: 'jhi-sale-point-detail',
    templateUrl: './sale-point-detail.component.html'
})
export class SalePointDetailComponent implements OnInit, OnDestroy {

    salePoint: SalePoint;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private salePointService: SalePointService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSalePoints();
    }

    load(id) {
        this.salePointService.find(id).subscribe((salePoint) => {
            this.salePoint = salePoint;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSalePoints() {
        this.eventSubscriber = this.eventManager.subscribe(
            'salePointListModification',
            (response) => this.load(this.salePoint.id)
        );
    }
}
