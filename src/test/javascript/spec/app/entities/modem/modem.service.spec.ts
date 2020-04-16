import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ModemService } from 'app/entities/modem/modem.service';
import { IModem, Modem } from 'app/shared/model/modem.model';

describe('Service Tests', () => {
  describe('Modem Service', () => {
    let injector: TestBed;
    let service: ModemService;
    let httpMock: HttpTestingController;
    let elemDefault: IModem;
    let expectedResult: IModem | IModem[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ModemService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Modem(0, 0, 0, 0, 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA', 0, 0, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Modem', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Modem()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Modem', () => {
        const returnedFromService = Object.assign(
          {
            crmEstacaoId: 1,
            vsatId: 1,
            vsatUid: 1,
            vsatGroup: 'BBBBBB',
            hub: 1,
            name: 'BBBBBB',
            ipAddress: 'BBBBBB',
            status: 1,
            statusDesc: 'BBBBBB',
            objectOID: 'BBBBBB',
            latitude: 1,
            longitude: 1,
            lanIpAddress: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Modem', () => {
        const returnedFromService = Object.assign(
          {
            crmEstacaoId: 1,
            vsatId: 1,
            vsatUid: 1,
            vsatGroup: 'BBBBBB',
            hub: 1,
            name: 'BBBBBB',
            ipAddress: 'BBBBBB',
            status: 1,
            statusDesc: 'BBBBBB',
            objectOID: 'BBBBBB',
            latitude: 1,
            longitude: 1,
            lanIpAddress: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Modem', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
