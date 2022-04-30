import dayjs from 'dayjs/esm';
import { IClassd } from 'app/entities/classd/classd.model';
import { IClassroom } from 'app/entities/classroom/classroom.model';
import { ITeacher } from 'app/entities/teacher/teacher.model';

export interface ICourse {
  id?: number;
  name?: string;
  introduce?: string;
  classTime?: dayjs.Dayjs;
  classds?: IClassd[] | null;
  classrooms?: IClassroom[] | null;
  teachers?: ITeacher[] | null;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public name?: string,
    public introduce?: string,
    public classTime?: dayjs.Dayjs,
    public classds?: IClassd[] | null,
    public classrooms?: IClassroom[] | null,
    public teachers?: ITeacher[] | null
  ) {}
}

export function getCourseIdentifier(course: ICourse): number | undefined {
  return course.id;
}
