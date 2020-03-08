import { ICommand } from 'app/shared/model/command.model';
import { BandMeter } from 'app/shared/model/enumerations/band-meter.model';

export interface IBand {
  id?: number;
  name?: string;
  bandMeter?: BandMeter;
  inUse?: boolean;
  commandRunningBand?: string;
  commands?: ICommand[];
}

export class Band implements IBand {
  constructor(
    public id?: number,
    public name?: string,
    public bandMeter?: BandMeter,
    public inUse?: boolean,
    public commandRunningBand?: string,
    public commands?: ICommand[]
  ) {
    this.inUse = this.inUse || false;
  }
}
