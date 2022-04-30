import { ICourse } from 'app/entities/course/course.model';

export interface ITeacher {
  id?: number;
  name?: string;
  age?: number;
  professional?: string;
  courses?: ICourse[] | null;
}

export class Teacher implements ITeacher {
  constructor(
    public id?: number,
    public name?: string,
    public age?: number,
    public professional?: string,
    public courses?: ICourse[] | null
  ) {}
}

export function getTeacherIdentifier(teacher: ITeacher): number | undefined {
  return teacher.id;
}
