import { ICourse } from 'app/entities/course/course.model';
import { IStudent } from 'app/entities/student/student.model';

export interface IClassd {
  id?: number;
  name?: string;
  courses?: ICourse[] | null;
  students?: IStudent[] | null;
}

export class Classd implements IClassd {
  constructor(public id?: number, public name?: string, public courses?: ICourse[] | null, public students?: IStudent[] | null) {}
}

export function getClassdIdentifier(classd: IClassd): number | undefined {
  return classd.id;
}
