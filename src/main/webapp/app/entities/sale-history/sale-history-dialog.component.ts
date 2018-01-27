import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SaleHistory } from './sale-history.model';
import { SaleHistoryPopupService } from './sale-history-popup.service';
import { SaleHistoryService } from './sale-history.service';
import { Tire, TireService } from '../tire';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sale-history-dialog',
    templateUrl: './sale-history-dialog.component.html'
})
export class SaleHistoryDialogComponent implements OnInit {

    saleHistory: SaleHistory;
    isSaving: boolean;

    tires: Tire[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private saleHistoryService: SaleHistoryService,
        private tireService: TireService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.tireService.query()
            .subscribe((res: ResponseWrapper) => { this.tires = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.saleHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.saleHistoryService.update(this.saleHistory));
        } else {
            this.subscribeToSaveResponse(
                this.saleHistoryService.create(this.saleHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<SaleHistory>) {
        result.subscribe((res: SaleHistory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SaleHistory) {
        this.eventManager.broadcast({ name: 'saleHistoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTireById(index: number, item: Tire) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sale-history-popup',
    template: ''
})
export class SaleHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saleHistoryPopupService: SaleHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.saleHistoryPopupService
                    .open(SaleHistoryDialogComponent as Component, params['id']);
            } else {
                this.saleHistoryPopupService
                    .open(SaleHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
