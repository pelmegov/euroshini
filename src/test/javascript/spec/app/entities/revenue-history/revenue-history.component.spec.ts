/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { EuroshiniTestModule } from '../../../test.module';
import { RevenueHistoryComponent } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history.component';
import { RevenueHistoryService } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history.service';
import { RevenueHistory } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history.model';

describe('Component Tests', () => {

    describe('RevenueHistory Management Component', () => {
        let comp: RevenueHistoryComponent;
        let fixture: ComponentFixture<RevenueHistoryComponent>;
        let service: RevenueHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [RevenueHistoryComponent],
                providers: [
                    RevenueHistoryService
                ]
            })
            .overrideTemplate(RevenueHistoryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RevenueHistoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RevenueHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new RevenueHistory(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.revenueHistories[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
