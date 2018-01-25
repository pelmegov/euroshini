import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Tire } from './tire.model';
import { TirePopupService } from './tire-popup.service';
import { TireService } from './tire.service';
import { SalePoint, SalePointService } from '../sale-point';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-tire-sale-dialog',
    templateUrl: './tire-sale-dialog.component.html'
})
export class TireSaleDialogComponent implements OnInit {

    tire: Tire;
    isSaving: boolean;

    salepoints: SalePoint[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private tireService: TireService,
        private salePointService: SalePointService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.salePointService.query()
            .subscribe((res: ResponseWrapper) => { this.salepoints = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    sale() {
        console.log('sale');
    }

    private subscribeToSaveResponse(result: Observable<Tire>) {
        result.subscribe((res: Tire) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Tire) {
        this.eventManager.broadcast({ name: 'tireListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
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

    constructor(
        private route: ActivatedRoute,
        private tirePopupService: TirePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
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
