/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { EuroshiniTestModule } from '../../../test.module';
import { SalePointComponent } from '../../../../../../main/webapp/app/entities/sale-point/sale-point.component';
import { SalePointService } from '../../../../../../main/webapp/app/entities/sale-point/sale-point.service';
import { SalePoint } from '../../../../../../main/webapp/app/entities/sale-point/sale-point.model';

describe('Component Tests', () => {

    describe('SalePoint Management Component', () => {
        let comp: SalePointComponent;
        let fixture: ComponentFixture<SalePointComponent>;
        let service: SalePointService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [SalePointComponent],
                providers: [
                    SalePointService
                ]
            })
            .overrideTemplate(SalePointComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalePointComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalePointService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new SalePoint(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.salePoints[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
