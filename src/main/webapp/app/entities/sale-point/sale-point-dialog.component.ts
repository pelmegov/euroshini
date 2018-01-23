import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SalePoint } from './sale-point.model';
import { SalePointPopupService } from './sale-point-popup.service';
import { SalePointService } from './sale-point.service';

@Component({
    selector: 'jhi-sale-point-dialog',
    templateUrl: './sale-point-dialog.component.html'
})
export class SalePointDialogComponent implements OnInit {

    salePoint: SalePoint;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private salePointService: SalePointService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.salePoint.id !== undefined) {
            this.subscribeToSaveResponse(
                this.salePointService.update(this.salePoint));
        } else {
            this.subscribeToSaveResponse(
                this.salePointService.create(this.salePoint));
        }
    }

    private subscribeToSaveResponse(result: Observable<SalePoint>) {
        result.subscribe((res: SalePoint) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SalePoint) {
        this.eventManager.broadcast({ name: 'salePointListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-sale-point-popup',
    template: ''
})
export class SalePointPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private salePointPopupService: SalePointPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.salePointPopupService
                    .open(SalePointDialogComponent as Component, params['id']);
            } else {
                this.salePointPopupService
                    .open(SalePointDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
