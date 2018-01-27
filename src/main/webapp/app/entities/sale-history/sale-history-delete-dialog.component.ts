import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SaleHistory } from './sale-history.model';
import { SaleHistoryPopupService } from './sale-history-popup.service';
import { SaleHistoryService } from './sale-history.service';

@Component({
    selector: 'jhi-sale-history-delete-dialog',
    templateUrl: './sale-history-delete-dialog.component.html'
})
export class SaleHistoryDeleteDialogComponent {

    saleHistory: SaleHistory;

    constructor(
        private saleHistoryService: SaleHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.saleHistoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'saleHistoryListModification',
                content: 'Deleted an saleHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sale-history-delete-popup',
    template: ''
})
export class SaleHistoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private saleHistoryPopupService: SaleHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.saleHistoryPopupService
                .open(SaleHistoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
