/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { EuroshiniTestModule } from '../../../test.module';
import { TireComponent } from '../../../../../../main/webapp/app/entities/tire/tire.component';
import { TireService } from '../../../../../../main/webapp/app/entities/tire/tire.service';
import { Tire } from '../../../../../../main/webapp/app/entities/tire/tire.model';

describe('Component Tests', () => {

    describe('Tire Management Component', () => {
        let comp: TireComponent;
        let fixture: ComponentFixture<TireComponent>;
        let service: TireService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [TireComponent],
                providers: [
                    TireService
                ]
            })
            .overrideTemplate(TireComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TireComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TireService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Tire(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.tires[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
