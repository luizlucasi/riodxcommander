import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LibraryTestModule } from '../../../test.module';
import { RadioComponent } from 'app/entities/radio/radio.component';
import { RadioService } from 'app/entities/radio/radio.service';
import { Radio } from 'app/shared/model/radio.model';

describe('Component Tests', () => {
  describe('Radio Management Component', () => {
    let comp: RadioComponent;
    let fixture: ComponentFixture<RadioComponent>;
    let service: RadioService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibraryTestModule],
        declarations: [RadioComponent],
      })
        .overrideTemplate(RadioComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RadioComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RadioService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Radio(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.radios && comp.radios[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
