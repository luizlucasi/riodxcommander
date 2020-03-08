export interface IOperator {
  id?: number;
  call?: string;
}

export class Operator implements IOperator {
  constructor(public id?: number, public call?: string) {}
}
