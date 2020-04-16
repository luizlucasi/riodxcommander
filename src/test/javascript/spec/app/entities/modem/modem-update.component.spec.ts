import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LibraryTestModule } from '../../../test.module';
import { ModemUpdateComponent } from 'app/entities/modem/modem-update.component';
import { ModemService } from 'app/entities/modem/modem.service';
import { Modem } from 'app/shared/model/modem.model';

describe('Component Tests', () => {
  describe('Modem Management Update Component', () => {
    let comp: ModemUpdateComponent;
    let fixture: ComponentFixture<ModemUpdateComponent>;
    let service: ModemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibraryTestModule],
        declarations: [ModemUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ModemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ModemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ModemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Modem(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Modem();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
