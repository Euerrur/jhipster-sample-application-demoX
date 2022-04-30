package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Classroom;
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
public class ClassroomRepositoryWithBagRelationshipsImpl implements ClassroomRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Classroom> fetchBagRelationships(Optional<Classroom> classroom) {
        return classroom.map(this::fetchCourses);
    }

    @Override
    public Page<Classroom> fetchBagRelationships(Page<Classroom> classrooms) {
        return new PageImpl<>(fetchBagRelationships(classrooms.getContent()), classrooms.getPageable(), classrooms.getTotalElements());
    }

    @Override
    public List<Classroom> fetchBagRelationships(List<Classroom> classrooms) {
        return Optional.of(classrooms).map(this::fetchCourses).orElse(Collections.emptyList());
    }

    Classroom fetchCourses(Classroom result) {
        return entityManager
            .createQuery(
                "select classroom from Classroom classroom left join fetch classroom.courses where classroom is :classroom",
                Classroom.class
            )
            .setParameter("classroom", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Classroom> fetchCourses(List<Classroom> classrooms) {
        return entityManager
            .createQuery(
                "select distinct classroom from Classroom classroom left join fetch classroom.courses where classroom in :classrooms",
                Classroom.class
            )
            .setParameter("classrooms", classrooms)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
