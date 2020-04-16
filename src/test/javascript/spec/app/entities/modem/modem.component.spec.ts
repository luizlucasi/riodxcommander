import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { LibraryTestModule } from '../../../test.module';
import { ModemComponent } from 'app/entities/modem/modem.component';
import { ModemService } from 'app/entities/modem/modem.service';
import { Modem } from 'app/shared/model/modem.model';

describe('Component Tests', () => {
  describe('Modem Management Component', () => {
    let comp: ModemComponent;
    let fixture: ComponentFixture<ModemComponent>;
    let service: ModemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibraryTestModule],
        declarations: [ModemComponent]
      })
        .overrideTemplate(ModemComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ModemComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ModemService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Modem(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.modems && comp.modems[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
