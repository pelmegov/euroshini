/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { EuroshiniTestModule } from '../../../test.module';
import { SaleHistoryDetailComponent } from '../../../../../../main/webapp/app/entities/sale-history/sale-history-detail.component';
import { SaleHistoryService } from '../../../../../../main/webapp/app/entities/sale-history/sale-history.service';
import { SaleHistory } from '../../../../../../main/webapp/app/entities/sale-history/sale-history.model';

describe('Component Tests', () => {

    describe('SaleHistory Management Detail Component', () => {
        let comp: SaleHistoryDetailComponent;
        let fixture: ComponentFixture<SaleHistoryDetailComponent>;
        let service: SaleHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [SaleHistoryDetailComponent],
                providers: [
                    SaleHistoryService
                ]
            })
            .overrideTemplate(SaleHistoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new SaleHistory(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.saleHistory).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
