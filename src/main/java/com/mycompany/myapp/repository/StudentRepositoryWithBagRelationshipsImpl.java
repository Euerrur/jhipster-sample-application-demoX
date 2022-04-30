package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Student;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class StudentRepositoryWithBagRelationshipsImpl implements StudentRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Student> fetchBagRelationships(Optional<Student> student) {
        return student.map(this::fetchClassds);
    }

    @Override
    public Page<Student> fetchBagRelationships(Page<Student> students) {
        return new PageImpl<>(fetchBagRelationships(students.getContent()), students.getPageable(), students.getTotalElements());
    }

    @Override
    public List<Student> fetchBagRelationships(List<Student> students) {
        return Optional.of(students).map(this::fetchClassds).orElse(Collections.emptyList());
    }

    Student fetchClassds(Student result) {
        return entityManager
            .createQuery("select student from Student student left join fetch student.classds where student is :student", Student.class)
            .setParameter("student", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Student> fetchClassds(List<Student> students) {
        return entityManager
            .createQuery(
                "select distinct student from Student student left join fetch student.classds where student in :students",
                Student.class
            )
            .setParameter("students", students)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
