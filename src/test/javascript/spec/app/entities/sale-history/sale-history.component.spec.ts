/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { EuroshiniTestModule } from '../../../test.module';
import { SaleHistoryComponent } from '../../../../../../main/webapp/app/entities/sale-history/sale-history.component';
import { SaleHistoryService } from '../../../../../../main/webapp/app/entities/sale-history/sale-history.service';
import { SaleHistory } from '../../../../../../main/webapp/app/entities/sale-history/sale-history.model';

describe('Component Tests', () => {

    describe('SaleHistory Management Component', () => {
        let comp: SaleHistoryComponent;
        let fixture: ComponentFixture<SaleHistoryComponent>;
        let service: SaleHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [SaleHistoryComponent],
                providers: [
                    SaleHistoryService
                ]
            })
            .overrideTemplate(SaleHistoryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleHistoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new SaleHistory(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.saleHistories[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
