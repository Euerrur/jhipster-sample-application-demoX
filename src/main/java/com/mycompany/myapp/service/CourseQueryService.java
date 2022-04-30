package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.repository.CourseRepository;
import com.mycompany.myapp.service.criteria.CourseCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Course} entities in the database.
 * The main input is a {@link CourseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Course} or a {@link Page} of {@link Course} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CourseQueryService extends QueryService<Course> {

    private final Logger log = LoggerFactory.getLogger(CourseQueryService.class);

    private final CourseRepository courseRepository;

    public CourseQueryService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Return a {@link List} of {@link Course} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Course> findByCriteria(CourseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Course> specification = createSpecification(criteria);
        return courseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Course} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Course> findByCriteria(CourseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Course> specification = createSpecification(criteria);
        return courseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CourseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Course> specification = createSpecification(criteria);
        return courseRepository.count(specification);
    }

    /**
     * Function to convert {@link CourseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Course> createSpecification(CourseCriteria criteria) {
        Specification<Course> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Course_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Course_.name));
            }
            if (criteria.getIntroduce() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIntroduce(), Course_.introduce));
            }
            if (criteria.getClassTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClassTime(), Course_.classTime));
            }
            if (criteria.getClassdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClassdId(), root -> root.join(Course_.classds, JoinType.LEFT).get(Classd_.id))
                    );
            }
            if (criteria.getClassroomId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassroomId(),
                            root -> root.join(Course_.classrooms, JoinType.LEFT).get(Classroom_.id)
                        )
                    );
            }
            if (criteria.getTeacherId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTeacherId(), root -> root.join(Course_.teachers, JoinType.LEFT).get(Teacher_.id))
                    );
            }
        }
        return specification;
    }
}
