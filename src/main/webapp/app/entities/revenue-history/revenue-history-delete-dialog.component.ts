import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RevenueHistory } from './revenue-history.model';
import { RevenueHistoryPopupService } from './revenue-history-popup.service';
import { RevenueHistoryService } from './revenue-history.service';

@Component({
    selector: 'jhi-revenue-history-delete-dialog',
    templateUrl: './revenue-history-delete-dialog.component.html'
})
export class RevenueHistoryDeleteDialogComponent {

    revenueHistory: RevenueHistory;

    constructor(
        private revenueHistoryService: RevenueHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.revenueHistoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'revenueHistoryListModification',
                content: 'Deleted an revenueHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-revenue-history-delete-popup',
    template: ''
})
export class RevenueHistoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private revenueHistoryPopupService: RevenueHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.revenueHistoryPopupService
                .open(RevenueHistoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
