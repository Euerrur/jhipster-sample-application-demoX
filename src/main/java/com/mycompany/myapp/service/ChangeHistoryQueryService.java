package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ChangeHistory;
import com.mycompany.myapp.repository.ChangeHistoryRepository;
import com.mycompany.myapp.service.criteria.ChangeHistoryCriteria;
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
 * Service for executing complex queries for {@link ChangeHistory} entities in the database.
 * The main input is a {@link ChangeHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChangeHistory} or a {@link Page} of {@link ChangeHistory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChangeHistoryQueryService extends QueryService<ChangeHistory> {

    private final Logger log = LoggerFactory.getLogger(ChangeHistoryQueryService.class);

    private final ChangeHistoryRepository changeHistoryRepository;

    public ChangeHistoryQueryService(ChangeHistoryRepository changeHistoryRepository) {
        this.changeHistoryRepository = changeHistoryRepository;
    }

    /**
     * Return a {@link List} of {@link ChangeHistory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChangeHistory> findByCriteria(ChangeHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChangeHistory> specification = createSpecification(criteria);
        return changeHistoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ChangeHistory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChangeHistory> findByCriteria(ChangeHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChangeHistory> specification = createSpecification(criteria);
        return changeHistoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChangeHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChangeHistory> specification = createSpecification(criteria);
        return changeHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ChangeHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChangeHistory> createSpecification(ChangeHistoryCriteria criteria) {
        Specification<ChangeHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ChangeHistory_.id));
            }
            if (criteria.getDescribe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescribe(), ChangeHistory_.describe));
            }
        }
        return specification;
    }
}
