/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { EuroshiniTestModule } from '../../../test.module';
import { SaleHistoryDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/sale-history/sale-history-delete-dialog.component';
import { SaleHistoryService } from '../../../../../../main/webapp/app/entities/sale-history/sale-history.service';

describe('Component Tests', () => {

    describe('SaleHistory Management Delete Component', () => {
        let comp: SaleHistoryDeleteDialogComponent;
        let fixture: ComponentFixture<SaleHistoryDeleteDialogComponent>;
        let service: SaleHistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EuroshiniTestModule],
                declarations: [SaleHistoryDeleteDialogComponent],
                providers: [
                    SaleHistoryService
                ]
            })
            .overrideTemplate(SaleHistoryDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SaleHistoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleHistoryService);
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
