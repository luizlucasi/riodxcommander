export interface IModem {
  id?: number;
  crmEstacaoId?: number;
  vsatId?: number;
  vsatUid?: number;
  vsatGroup?: string;
  hub?: number;
  name?: string;
  ipAddress?: string;
  status?: number;
  statusDesc?: string;
  objectOID?: string;
  latitude?: number;
  longitude?: number;
  lanIpAddress?: string;
}

export class Modem implements IModem {
  constructor(
    public id?: number,
    public crmEstacaoId?: number,
    public vsatId?: number,
    public vsatUid?: number,
    public vsatGroup?: string,
    public hub?: number,
    public name?: string,
    public ipAddress?: string,
    public status?: number,
    public statusDesc?: string,
    public objectOID?: string,
    public latitude?: number,
    public longitude?: number,
    public lanIpAddress?: string
  ) {}
}
