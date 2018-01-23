/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { EuroshiniTestModule } from '../../../test.module';
import { SalePointDetailComponent } from '../../../../../../main/webapp/app/entities/sale-point/sale-point-detail.component';
import { SalePointService } from '../../../../../../main/webapp/app/entities/sale-point/sale-point.service';
import { SalePoint } from '../../../../../../main/webapp/app/entities/sale-point/sale-point.model';

describe('Component Tests', () => {

    describe('SalePoint Management Detail Component', () => {
        let comp: SalePointDetailComponent;
        let fixture: ComponentFixture<SalePointDetailComponent>;
        let service: SalePointService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [SalePointDetailComponent],
                providers: [
                    SalePointService
                ]
            })
            .overrideTemplate(SalePointDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalePointDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalePointService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new SalePoint(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.salePoint).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
