import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Tire } from './tire.model';
import { TireService } from './tire.service';

@Component({
    selector: 'jhi-tire-detail',
    templateUrl: './tire-detail.component.html'
})
export class TireDetailComponent implements OnInit, OnDestroy {

    tire: Tire;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private tireService: TireService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTires();
    }

    load(id) {
        this.tireService.find(id).subscribe((tire) => {
            this.tire = tire;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTires() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tireListModification',
            (response) => this.load(this.tire.id)
        );
    }
}
