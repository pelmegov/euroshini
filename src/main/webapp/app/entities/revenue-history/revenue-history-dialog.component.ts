import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { RevenueHistory } from './revenue-history.model';
import { RevenueHistoryPopupService } from './revenue-history-popup.service';
import { RevenueHistoryService } from './revenue-history.service';
import { Tire, TireService } from '../tire';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-revenue-history-dialog',
    templateUrl: './revenue-history-dialog.component.html'
})
export class RevenueHistoryDialogComponent implements OnInit {

    revenueHistory: RevenueHistory;
    isSaving: boolean;

    tires: Tire[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private revenueHistoryService: RevenueHistoryService,
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
        if (this.revenueHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.revenueHistoryService.update(this.revenueHistory));
        } else {
            this.subscribeToSaveResponse(
                this.revenueHistoryService.create(this.revenueHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<RevenueHistory>) {
        result.subscribe((res: RevenueHistory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: RevenueHistory) {
        this.eventManager.broadcast({ name: 'revenueHistoryListModification', content: 'OK'});
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
    selector: 'jhi-revenue-history-popup',
    template: ''
})
export class RevenueHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private revenueHistoryPopupService: RevenueHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.revenueHistoryPopupService
                    .open(RevenueHistoryDialogComponent as Component, params['id']);
            } else {
                this.revenueHistoryPopupService
                    .open(RevenueHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
