/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { EuroshiniTestModule } from '../../../test.module';
import { SalePointDialogComponent } from '../../../../../../main/webapp/app/entities/sale-point/sale-point-dialog.component';
import { SalePointService } from '../../../../../../main/webapp/app/entities/sale-point/sale-point.service';
import { SalePoint } from '../../../../../../main/webapp/app/entities/sale-point/sale-point.model';

describe('Component Tests', () => {

    describe('SalePoint Management Dialog Component', () => {
        let comp: SalePointDialogComponent;
        let fixture: ComponentFixture<SalePointDialogComponent>;
        let service: SalePointService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [SalePointDialogComponent],
                providers: [
                    SalePointService
                ]
            })
            .overrideTemplate(SalePointDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalePointDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalePointService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SalePoint(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.salePoint = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'salePointListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SalePoint();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.salePoint = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'salePointListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
