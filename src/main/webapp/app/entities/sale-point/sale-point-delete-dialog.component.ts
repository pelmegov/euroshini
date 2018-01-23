import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SalePoint } from './sale-point.model';
import { SalePointPopupService } from './sale-point-popup.service';
import { SalePointService } from './sale-point.service';

@Component({
    selector: 'jhi-sale-point-delete-dialog',
    templateUrl: './sale-point-delete-dialog.component.html'
})
export class SalePointDeleteDialogComponent {

    salePoint: SalePoint;

    constructor(
        private salePointService: SalePointService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.salePointService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'salePointListModification',
                content: 'Deleted an salePoint'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sale-point-delete-popup',
    template: ''
})
export class SalePointDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private salePointPopupService: SalePointPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.salePointPopupService
                .open(SalePointDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
