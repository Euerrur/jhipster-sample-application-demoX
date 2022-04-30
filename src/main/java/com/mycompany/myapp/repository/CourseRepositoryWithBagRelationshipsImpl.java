package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Course;
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
public class CourseRepositoryWithBagRelationshipsImpl implements CourseRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Course> fetchBagRelationships(Optional<Course> course) {
        return course.map(this::fetchClassds);
    }

    @Override
    public Page<Course> fetchBagRelationships(Page<Course> courses) {
        return new PageImpl<>(fetchBagRelationships(courses.getContent()), courses.getPageable(), courses.getTotalElements());
    }

    @Override
    public List<Course> fetchBagRelationships(List<Course> courses) {
        return Optional.of(courses).map(this::fetchClassds).orElse(Collections.emptyList());
    }

    Course fetchClassds(Course result) {
        return entityManager
            .createQuery("select course from Course course left join fetch course.classds where course is :course", Course.class)
            .setParameter("course", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Course> fetchClassds(List<Course> courses) {
        return entityManager
            .createQuery("select distinct course from Course course left join fetch course.classds where course in :courses", Course.class)
            .setParameter("courses", courses)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
