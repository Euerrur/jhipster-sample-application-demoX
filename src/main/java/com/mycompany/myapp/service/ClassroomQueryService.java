package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Classroom;
import com.mycompany.myapp.repository.ClassroomRepository;
import com.mycompany.myapp.service.criteria.ClassroomCriteria;
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
 * Service for executing complex queries for {@link Classroom} entities in the database.
 * The main input is a {@link ClassroomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Classroom} or a {@link Page} of {@link Classroom} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassroomQueryService extends QueryService<Classroom> {

    private final Logger log = LoggerFactory.getLogger(ClassroomQueryService.class);

    private final ClassroomRepository classroomRepository;

    public ClassroomQueryService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    /**
     * Return a {@link List} of {@link Classroom} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Classroom> findByCriteria(ClassroomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Classroom> specification = createSpecification(criteria);
        return classroomRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Classroom} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Classroom> findByCriteria(ClassroomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Classroom> specification = createSpecification(criteria);
        return classroomRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassroomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Classroom> specification = createSpecification(criteria);
        return classroomRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassroomCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Classroom> createSpecification(ClassroomCriteria criteria) {
        Specification<Classroom> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Classroom_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Classroom_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Classroom_.address));
            }
            if (criteria.getCourseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCourseId(), root -> root.join(Classroom_.courses, JoinType.LEFT).get(Course_.id))
                    );
            }
        }
        return specification;
    }
}
