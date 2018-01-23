import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Tire } from './tire.model';
import { TirePopupService } from './tire-popup.service';
import { TireService } from './tire.service';

@Component({
    selector: 'jhi-tire-delete-dialog',
    templateUrl: './tire-delete-dialog.component.html'
})
export class TireDeleteDialogComponent {

    tire: Tire;

    constructor(
        private tireService: TireService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tireService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tireListModification',
                content: 'Deleted an tire'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tire-delete-popup',
    template: ''
})
export class TireDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tirePopupService: TirePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tirePopupService
                .open(TireDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
