export interface IRadio {
  id?: number;
  description?: string;
}

export class Radio implements IRadio {
  constructor(public id?: number, public description?: string) {}
}
