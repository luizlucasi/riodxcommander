import { IOperator } from 'app/shared/model/operator.model';
import { IBand } from 'app/shared/model/band.model';

export interface ICommand {
  id?: number;
  call?: string;
  inUse?: boolean;
  operator?: IOperator;
  bands?: IBand[];
}

export class Command implements ICommand {
  constructor(public id?: number, public call?: string, public inUse?: boolean, public operator?: IOperator, public bands?: IBand[]) {
    this.inUse = this.inUse || false;
  }
}
