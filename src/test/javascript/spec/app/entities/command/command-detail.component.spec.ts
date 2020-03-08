import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LibraryTestModule } from '../../../test.module';
import { CommandDetailComponent } from 'app/entities/command/command-detail.component';
import { Command } from 'app/shared/model/command.model';

describe('Component Tests', () => {
  describe('Command Management Detail Component', () => {
    let comp: CommandDetailComponent;
    let fixture: ComponentFixture<CommandDetailComponent>;
    const route = ({ data: of({ command: new Command(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibraryTestModule],
        declarations: [CommandDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CommandDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CommandDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load command on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.command).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
