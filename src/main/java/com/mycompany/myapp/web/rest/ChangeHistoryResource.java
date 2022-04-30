package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ChangeHistory;
import com.mycompany.myapp.repository.ChangeHistoryRepository;
import com.mycompany.myapp.service.ChangeHistoryQueryService;
import com.mycompany.myapp.service.ChangeHistoryService;
import com.mycompany.myapp.service.criteria.ChangeHistoryCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ChangeHistory}.
 */
@RestController
@RequestMapping("/api")
public class ChangeHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ChangeHistoryResource.class);

    private static final String ENTITY_NAME = "changeHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChangeHistoryService changeHistoryService;

    private final ChangeHistoryRepository changeHistoryRepository;

    private final ChangeHistoryQueryService changeHistoryQueryService;

    public ChangeHistoryResource(
        ChangeHistoryService changeHistoryService,
        ChangeHistoryRepository changeHistoryRepository,
        ChangeHistoryQueryService changeHistoryQueryService
    ) {
        this.changeHistoryService = changeHistoryService;
        this.changeHistoryRepository = changeHistoryRepository;
        this.changeHistoryQueryService = changeHistoryQueryService;
    }

    /**
     * {@code POST  /change-histories} : Create a new changeHistory.
     *
     * @param changeHistory the changeHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new changeHistory, or with status {@code 400 (Bad Request)} if the changeHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/change-histories")
    public ResponseEntity<ChangeHistory> createChangeHistory(@Valid @RequestBody ChangeHistory changeHistory) throws URISyntaxException {
        log.debug("REST request to save ChangeHistory : {}", changeHistory);
        if (changeHistory.getId() != null) {
            throw new BadRequestAlertException("A new changeHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChangeHistory result = changeHistoryService.save(changeHistory);
        return ResponseEntity
            .created(new URI("/api/change-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /change-histories/:id} : Updates an existing changeHistory.
     *
     * @param id the id of the changeHistory to save.
     * @param changeHistory the changeHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated changeHistory,
     * or with status {@code 400 (Bad Request)} if the changeHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the changeHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/change-histories/{id}")
    public ResponseEntity<ChangeHistory> updateChangeHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChangeHistory changeHistory
    ) throws URISyntaxException {
        log.debug("REST request to update ChangeHistory : {}, {}", id, changeHistory);
        if (changeHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, changeHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!changeHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChangeHistory result = changeHistoryService.update(changeHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, changeHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /change-histories/:id} : Partial updates given fields of an existing changeHistory, field will ignore if it is null
     *
     * @param id the id of the changeHistory to save.
     * @param changeHistory the changeHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated changeHistory,
     * or with status {@code 400 (Bad Request)} if the changeHistory is not valid,
     * or with status {@code 404 (Not Found)} if the changeHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the changeHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/change-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChangeHistory> partialUpdateChangeHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChangeHistory changeHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChangeHistory partially : {}, {}", id, changeHistory);
        if (changeHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, changeHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!changeHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChangeHistory> result = changeHistoryService.partialUpdate(changeHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, changeHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /change-histories} : get all the changeHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of changeHistories in body.
     */
    @GetMapping("/change-histories")
    public ResponseEntity<List<ChangeHistory>> getAllChangeHistories(ChangeHistoryCriteria criteria) {
        log.debug("REST request to get ChangeHistories by criteria: {}", criteria);
        List<ChangeHistory> entityList = changeHistoryQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /change-histories/count} : count all the changeHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/change-histories/count")
    public ResponseEntity<Long> countChangeHistories(ChangeHistoryCriteria criteria) {
        log.debug("REST request to count ChangeHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(changeHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /change-histories/:id} : get the "id" changeHistory.
     *
     * @param id the id of the changeHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the changeHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/change-histories/{id}")
    public ResponseEntity<ChangeHistory> getChangeHistory(@PathVariable Long id) {
        log.debug("REST request to get ChangeHistory : {}", id);
        Optional<ChangeHistory> changeHistory = changeHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(changeHistory);
    }

    /**
     * {@code DELETE  /change-histories/:id} : delete the "id" changeHistory.
     *
     * @param id the id of the changeHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/change-histories/{id}")
    public ResponseEntity<Void> deleteChangeHistory(@PathVariable Long id) {
        log.debug("REST request to delete ChangeHistory : {}", id);
        changeHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
