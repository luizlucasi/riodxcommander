import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LibraryTestModule } from '../../../test.module';
import { ModemDetailComponent } from 'app/entities/modem/modem-detail.component';
import { Modem } from 'app/shared/model/modem.model';

describe('Component Tests', () => {
  describe('Modem Management Detail Component', () => {
    let comp: ModemDetailComponent;
    let fixture: ComponentFixture<ModemDetailComponent>;
    const route = ({ data: of({ modem: new Modem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibraryTestModule],
        declarations: [ModemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ModemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ModemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load modem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.modem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
