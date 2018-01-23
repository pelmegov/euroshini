/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { EuroshiniTestModule } from '../../../test.module';
import { TireDialogComponent } from '../../../../../../main/webapp/app/entities/tire/tire-dialog.component';
import { TireService } from '../../../../../../main/webapp/app/entities/tire/tire.service';
import { Tire } from '../../../../../../main/webapp/app/entities/tire/tire.model';
import { SalePointService } from '../../../../../../main/webapp/app/entities/sale-point';

describe('Component Tests', () => {

    describe('Tire Management Dialog Component', () => {
        let comp: TireDialogComponent;
        let fixture: ComponentFixture<TireDialogComponent>;
        let service: TireService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [TireDialogComponent],
                providers: [
                    SalePointService,
                    TireService
                ]
            })
            .overrideTemplate(TireDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TireDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TireService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Tire(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.tire = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'tireListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Tire();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.tire = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'tireListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
