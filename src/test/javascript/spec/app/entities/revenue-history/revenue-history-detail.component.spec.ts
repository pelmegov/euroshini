/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { EuroshiniTestModule } from '../../../test.module';
import { RevenueHistoryDetailComponent } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history-detail.component';
import { RevenueHistoryService } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history.service';
import { RevenueHistory } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history.model';

describe('Component Tests', () => {

    describe('RevenueHistory Management Detail Component', () => {
        let comp: RevenueHistoryDetailComponent;
        let fixture: ComponentFixture<RevenueHistoryDetailComponent>;
        let service: RevenueHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [RevenueHistoryDetailComponent],
                providers: [
                    RevenueHistoryService
                ]
            })
            .overrideTemplate(RevenueHistoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RevenueHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RevenueHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new RevenueHistory(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.revenueHistory).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
