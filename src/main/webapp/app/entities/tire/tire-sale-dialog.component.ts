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
import {SaleHistory, SaleHistoryService} from '../sale-history';

@Component({
    selector: 'jhi-tire-sale-dialog',
    templateUrl: './tire-sale-dialog.component.html'
})
export class TireSaleDialogComponent implements OnInit {

    tire: Tire;
    count: number;
    price: number;
    informationOnSale: string;
    saleHistoryPoint: SaleHistory = new SaleHistory();

    isSaling: boolean;

    salepoints: SalePoint[];

    constructor(public activeModal: NgbActiveModal,
                private jhiAlertService: JhiAlertService,
                private tireService: TireService,
                private saleHistoryService: SaleHistoryService,
                private salePointService: SalePointService,
                private eventManager: JhiEventManager,
                ) {
    }

    ngOnInit() {
        this.isSaling = false;
        this.price = this.tire.price;
        this.salePointService.query()
            .subscribe((res: ResponseWrapper) => {
                this.salepoints = res.json;
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    sale() {
        if (this.moreTiresThanOnSalePoint() || this.count === undefined || this.price === undefined) {
            this.informationOnSale = 'Error. Please, change parameters for sale.';
            return;
        }
        this.updateTireCount();
        this.createSaleHistoryPoint();
        this.activeModal.close(true);
    }

    onKey(event: Event) {
        if (!this.moreTiresThanOnSalePoint()) {
            this.informationOnSale = 'Current sum: ' + this.price * this.count;
        }
    }

    private updateTireCount() {
        this.tire.count -= this.count;
        this.tireService.update(this.tire).subscribe((tire) => {
            this.tire = tire;
        });
    }

    private createSaleHistoryPoint() {
        this.saleHistoryPoint.price = this.price;
        this.saleHistoryPoint.count = this.count;
        this.saleHistoryPoint.tire = this.tire;
        this.saleHistoryPoint.sum = this.price * this.count;

        this.isSaling = true;

        if (this.saleHistoryPoint.id !== undefined) {
            this.subscribeToSaveResponse(
                this.saleHistoryService.update(this.saleHistoryPoint));
        } else {
            this.subscribeToSaveResponse(
                this.saleHistoryService.create(this.saleHistoryPoint));
        }
    }

    private moreTiresThanOnSalePoint() {
        if (this.count > this.tire.count) {
            this.onSaleError();
            this.informationOnSale = 'There are not enough tires on the point of sale.';
            return true;
        }
        return false;
    }

    private subscribeToSaveResponse(result: Observable<SaleHistory>) {
        result.subscribe((res: SaleHistory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaleError());
    }

    private onSaveSuccess(result: SaleHistory) {
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
    selector: 'jhi-tire-sale-popup',
    template: ''
})
export class TireSalePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(private route: ActivatedRoute,
                private tirePopupService: TirePopupService) {
    }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.tirePopupService
                    .open(TireSaleDialogComponent as Component, params['id']);
            } else {
                this.tirePopupService
                    .open(TireSaleDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
