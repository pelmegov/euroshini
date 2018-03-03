import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Tire } from './tire.model';
import { TireService } from './tire.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-tire',
    templateUrl: './tire.component.html'
})
export class TireComponent implements OnInit, OnDestroy {

    currentAccount: any;
    tires: Tire[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    tireCount: number;

    salePointId: number;

    constructor(
        private tireService: TireService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.salePointId = this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['salePointId'] ?
            this.activatedRoute.snapshot.queryParams['salePointId'] : '';
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        const criteria = this.getSalePointCriteria();

        if (this.currentSearch) {
            this.tireService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort(),
                criteria,
            }).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        }
        this.tireService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
            criteria,
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
        this.getTireCount();
    }

    private getSalePointCriteria() {
        const criteria = [];
        criteria.push({key: 'salePointId.equals', value: this.salePointId});
        return criteria;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        const criteria = this.getSalePointCriteria();
        this.router.navigate(['/tire'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
                criteria
            }
        });
        this.loadAll();
    }

    getTireCount() {
        this.tireService.getTireCount().subscribe(
            (res: ResponseWrapper) => {
                this.tireCount = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    clear() {
        const criteria = this.getSalePointCriteria();
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/tire', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
            criteria
        }]);
        this.loadAll();
    }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        const criteria = this.getSalePointCriteria();
        this.router.navigate(['/tire', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
            criteria
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTires();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Tire) {
        return item.id;
    }
    registerChangeInTires() {
        this.eventSubscriber = this.eventManager.subscribe('tireListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.tires = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
