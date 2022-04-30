import { IClassd } from 'app/entities/classd/classd.model';
import { GradeLevelType } from 'app/entities/enumerations/grade-level-type.model';

export interface IStudent {
  id?: number;
  name?: string;
  age?: number;
  number?: number;
  grade?: GradeLevelType;
  classds?: IClassd[] | null;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public name?: string,
    public age?: number,
    public number?: number,
    public grade?: GradeLevelType,
    public classds?: IClassd[] | null
  ) {}
}

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
