/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { EuroshiniTestModule } from '../../../test.module';
import { SaleHistoryDialogComponent } from '../../../../../../main/webapp/app/entities/sale-history/sale-history-dialog.component';
import { SaleHistoryService } from '../../../../../../main/webapp/app/entities/sale-history/sale-history.service';
import { SaleHistory } from '../../../../../../main/webapp/app/entities/sale-history/sale-history.model';
import { TireService } from '../../../../../../main/webapp/app/entities/tire';

describe('Component Tests', () => {

    describe('SaleHistory Management Dialog Component', () => {
        let comp: SaleHistoryDialogComponent;
        let fixture: ComponentFixture<SaleHistoryDialogComponent>;
        let service: SaleHistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [SaleHistoryDialogComponent],
                providers: [
                    TireService,
                    SaleHistoryService
                ]
            })
            .overrideTemplate(SaleHistoryDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleHistoryDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleHistoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SaleHistory(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.saleHistory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'saleHistoryListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SaleHistory();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.saleHistory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'saleHistoryListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
