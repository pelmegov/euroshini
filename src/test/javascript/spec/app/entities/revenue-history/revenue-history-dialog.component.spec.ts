/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { EuroshiniTestModule } from '../../../test.module';
import { RevenueHistoryDialogComponent } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history-dialog.component';
import { RevenueHistoryService } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history.service';
import { RevenueHistory } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history.model';
import { TireService } from '../../../../../../main/webapp/app/entities/tire';

describe('Component Tests', () => {

    describe('RevenueHistory Management Dialog Component', () => {
        let comp: RevenueHistoryDialogComponent;
        let fixture: ComponentFixture<RevenueHistoryDialogComponent>;
        let service: RevenueHistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [RevenueHistoryDialogComponent],
                providers: [
                    TireService,
                    RevenueHistoryService
                ]
            })
            .overrideTemplate(RevenueHistoryDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RevenueHistoryDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RevenueHistoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new RevenueHistory(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.revenueHistory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'revenueHistoryListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new RevenueHistory();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.revenueHistory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'revenueHistoryListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
