/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { EuroshiniTestModule } from '../../../test.module';
import { TireDetailComponent } from '../../../../../../main/webapp/app/entities/tire/tire-detail.component';
import { TireService } from '../../../../../../main/webapp/app/entities/tire/tire.service';
import { Tire } from '../../../../../../main/webapp/app/entities/tire/tire.model';

describe('Component Tests', () => {

    describe('Tire Management Detail Component', () => {
        let comp: TireDetailComponent;
        let fixture: ComponentFixture<TireDetailComponent>;
        let service: TireService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [TireDetailComponent],
                providers: [
                    TireService
                ]
            })
            .overrideTemplate(TireDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TireDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TireService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Tire(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.tire).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
