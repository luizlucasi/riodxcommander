import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LibraryTestModule } from '../../../test.module';
import { RadioUpdateComponent } from 'app/entities/radio/radio-update.component';
import { RadioService } from 'app/entities/radio/radio.service';
import { Radio } from 'app/shared/model/radio.model';

describe('Component Tests', () => {
  describe('Radio Management Update Component', () => {
    let comp: RadioUpdateComponent;
    let fixture: ComponentFixture<RadioUpdateComponent>;
    let service: RadioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibraryTestModule],
        declarations: [RadioUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RadioUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RadioUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RadioService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Radio(123);
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
        const entity = new Radio();
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
