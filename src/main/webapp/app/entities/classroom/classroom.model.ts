import { ICourse } from 'app/entities/course/course.model';

export interface IClassroom {
  id?: number;
  name?: string;
  address?: string;
  courses?: ICourse[] | null;
}

export class Classroom implements IClassroom {
  constructor(public id?: number, public name?: string, public address?: string, public courses?: ICourse[] | null) {}
}

export function getClassroomIdentifier(classroom: IClassroom): number | undefined {
  return classroom.id;
}
