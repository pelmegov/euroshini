/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { EuroshiniTestModule } from '../../../test.module';
import { RevenueHistoryDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history-delete-dialog.component';
import { RevenueHistoryService } from '../../../../../../main/webapp/app/entities/revenue-history/revenue-history.service';

describe('Component Tests', () => {

    describe('RevenueHistory Management Delete Component', () => {
        let comp: RevenueHistoryDeleteDialogComponent;
        let fixture: ComponentFixture<RevenueHistoryDeleteDialogComponent>;
        let service: RevenueHistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [RevenueHistoryDeleteDialogComponent],
                providers: [
                    RevenueHistoryService
                ]
            })
            .overrideTemplate(RevenueHistoryDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RevenueHistoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RevenueHistoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
