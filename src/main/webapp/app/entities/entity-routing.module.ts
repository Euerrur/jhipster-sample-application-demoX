import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'classd',
        data: { pageTitle: 'jhipsterApp.classd.home.title' },
        loadChildren: () => import('./classd/classd.module').then(m => m.ClassdModule),
      },
      {
        path: 'classroom',
        data: { pageTitle: 'jhipsterApp.classroom.home.title' },
        loadChildren: () => import('./classroom/classroom.module').then(m => m.ClassroomModule),
      },
      {
        path: 'student',
        data: { pageTitle: 'jhipsterApp.student.home.title' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      {
        path: 'course',
        data: { pageTitle: 'jhipsterApp.course.home.title' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      {
        path: 'teacher',
        data: { pageTitle: 'jhipsterApp.teacher.home.title' },
        loadChildren: () => import('./teacher/teacher.module').then(m => m.TeacherModule),
      },
      {
        path: 'change-history',
        data: { pageTitle: 'jhipsterApp.changeHistory.home.title' },
        loadChildren: () => import('./change-history/change-history.module').then(m => m.ChangeHistoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
