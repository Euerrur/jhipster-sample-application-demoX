export interface IChangeHistory {
  id?: number;
  describe?: string;
}

export class ChangeHistory implements IChangeHistory {
  constructor(public id?: number, public describe?: string) {}
}

export function getChangeHistoryIdentifier(changeHistory: IChangeHistory): number | undefined {
  return changeHistory.id;
}
