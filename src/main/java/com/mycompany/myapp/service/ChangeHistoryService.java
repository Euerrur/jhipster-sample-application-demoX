package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ChangeHistory;
import com.mycompany.myapp.repository.ChangeHistoryRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ChangeHistory}.
 */
@Service
@Transactional
public class ChangeHistoryService {

    private final Logger log = LoggerFactory.getLogger(ChangeHistoryService.class);

    private final ChangeHistoryRepository changeHistoryRepository;

    public ChangeHistoryService(ChangeHistoryRepository changeHistoryRepository) {
        this.changeHistoryRepository = changeHistoryRepository;
    }

    /**
     * Save a changeHistory.
     *
     * @param changeHistory the entity to save.
     * @return the persisted entity.
     */
    public ChangeHistory save(ChangeHistory changeHistory) {
        log.debug("Request to save ChangeHistory : {}", changeHistory);
        return changeHistoryRepository.save(changeHistory);
    }

    /**
     * Update a changeHistory.
     *
     * @param changeHistory the entity to save.
     * @return the persisted entity.
     */
    public ChangeHistory update(ChangeHistory changeHistory) {
        log.debug("Request to save ChangeHistory : {}", changeHistory);
        return changeHistoryRepository.save(changeHistory);
    }

    /**
     * Partially update a changeHistory.
     *
     * @param changeHistory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChangeHistory> partialUpdate(ChangeHistory changeHistory) {
        log.debug("Request to partially update ChangeHistory : {}", changeHistory);

        return changeHistoryRepository
            .findById(changeHistory.getId())
            .map(existingChangeHistory -> {
                if (changeHistory.getDescribe() != null) {
                    existingChangeHistory.setDescribe(changeHistory.getDescribe());
                }

                return existingChangeHistory;
            })
            .map(changeHistoryRepository::save);
    }

    /**
     * Get all the changeHistories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ChangeHistory> findAll() {
        log.debug("Request to get all ChangeHistories");
        return changeHistoryRepository.findAll();
    }

    /**
     * Get one changeHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChangeHistory> findOne(Long id) {
        log.debug("Request to get ChangeHistory : {}", id);
        return changeHistoryRepository.findById(id);
    }

    /**
     * Delete the changeHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChangeHistory : {}", id);
        changeHistoryRepository.deleteById(id);
    }
}
