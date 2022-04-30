package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Classd;
import com.mycompany.myapp.repository.ClassdRepository;
import com.mycompany.myapp.service.criteria.ClassdCriteria;
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
 * Service for executing complex queries for {@link Classd} entities in the database.
 * The main input is a {@link ClassdCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Classd} or a {@link Page} of {@link Classd} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassdQueryService extends QueryService<Classd> {

    private final Logger log = LoggerFactory.getLogger(ClassdQueryService.class);

    private final ClassdRepository classdRepository;

    public ClassdQueryService(ClassdRepository classdRepository) {
        this.classdRepository = classdRepository;
    }

    /**
     * Return a {@link List} of {@link Classd} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Classd> findByCriteria(ClassdCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Classd> specification = createSpecification(criteria);
        return classdRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Classd} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Classd> findByCriteria(ClassdCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Classd> specification = createSpecification(criteria);
        return classdRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassdCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Classd> specification = createSpecification(criteria);
        return classdRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassdCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Classd> createSpecification(ClassdCriteria criteria) {
        Specification<Classd> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Classd_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Classd_.name));
            }
            if (criteria.getCourseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCourseId(), root -> root.join(Classd_.courses, JoinType.LEFT).get(Course_.id))
                    );
            }
            if (criteria.getStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStudentId(), root -> root.join(Classd_.students, JoinType.LEFT).get(Student_.id))
                    );
            }
        }
        return specification;
    }
}
