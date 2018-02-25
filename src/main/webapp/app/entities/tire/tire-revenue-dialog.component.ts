import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {Observable} from 'rxjs/Observable';

import {Tire} from './tire.model';
import {TirePopupService} from './tire-popup.service';
import {TireService} from './tire.service';
import {SalePoint, SalePointService} from '../sale-point';
import {ResponseWrapper} from '../../shared';
import {RevenueHistory, RevenueHistoryService} from '../revenue-history';

@Component({
    selector: 'jhi-tire-revenue-dialog',
    templateUrl: './tire-revenue-dialog.component.html'
})
export class TireRevenueDialogComponent implements OnInit {

    tire: Tire;
    count: number;

    revenueHistoryPoint: RevenueHistory = new RevenueHistory();

    isSaling: boolean;
    isReturn: boolean;

    salepoints: SalePoint[];

    constructor(public activeModal: NgbActiveModal,
                private jhiAlertService: JhiAlertService,
                private tireService: TireService,
                private revenueHistoryService: RevenueHistoryService,
                private salePointService: SalePointService,
                private eventManager: JhiEventManager,
                ) {
    }

    ngOnInit() {
        this.isSaling = false;

        this.salePointService.query()
            .subscribe((res: ResponseWrapper) => {
                this.salepoints = res.json;
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    revenue() {
        this.updateTireCount();
        if (!this.isReturn) {
            this.createRevenueHistoryPoint();
        }
        this.activeModal.close(true);
    }

    private updateTireCount() {
        this.tire.count += this.count;
        this.tireService.update(this.tire).subscribe((tire) => {
            this.tire = tire;
        });
    }

    private createRevenueHistoryPoint() {
        this.revenueHistoryPoint.count = this.count;
        this.revenueHistoryPoint.tire = this.tire;

        this.isSaling = true;

        if (this.revenueHistoryPoint.id !== undefined) {
            this.subscribeToSaveResponse(
                this.revenueHistoryService.update(this.revenueHistoryPoint));
        } else {
            this.subscribeToSaveResponse(
                this.revenueHistoryService.create(this.revenueHistoryPoint));
        }
    }

    private subscribeToSaveResponse(result: Observable<RevenueHistory>) {
        result.subscribe((res: RevenueHistory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaleError());
    }

    private onSaveSuccess(result: RevenueHistory) {
        this.eventManager.broadcast({name: 'tireListModification', content: 'OK'});
        this.eventManager.broadcast({name: 'saleHistoryListModification', content: 'OK'});
        this.isSaling = false;
        this.activeModal.dismiss(result);
    }

    private onSaleError() {
        this.isSaling = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSalePointById(index: number, item: SalePoint) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-tire-revenue-popup',
    template: ''
})
export class TireRevenuePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private tirePopupService: TirePopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.tirePopupService
                    .open(TireRevenueDialogComponent as Component, params['id']);
            } else {
                this.tirePopupService
                    .open(TireRevenueDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
