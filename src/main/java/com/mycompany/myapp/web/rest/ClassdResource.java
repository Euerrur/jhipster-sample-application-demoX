package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Classd;
import com.mycompany.myapp.repository.ClassdRepository;
import com.mycompany.myapp.service.ClassdQueryService;
import com.mycompany.myapp.service.ClassdService;
import com.mycompany.myapp.service.criteria.ClassdCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Classd}.
 */
@RestController
@RequestMapping("/api")
public class ClassdResource {

    private final Logger log = LoggerFactory.getLogger(ClassdResource.class);

    private static final String ENTITY_NAME = "classd";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassdService classdService;

    private final ClassdRepository classdRepository;

    private final ClassdQueryService classdQueryService;

    public ClassdResource(ClassdService classdService, ClassdRepository classdRepository, ClassdQueryService classdQueryService) {
        this.classdService = classdService;
        this.classdRepository = classdRepository;
        this.classdQueryService = classdQueryService;
    }

    /**
     * {@code POST  /classds} : Create a new classd.
     *
     * @param classd the classd to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classd, or with status {@code 400 (Bad Request)} if the classd has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classds")
    public ResponseEntity<Classd> createClassd(@Valid @RequestBody Classd classd) throws URISyntaxException {
        log.debug("REST request to save Classd : {}", classd);
        if (classd.getId() != null) {
            throw new BadRequestAlertException("A new classd cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Classd result = classdService.save(classd);
        return ResponseEntity
            .created(new URI("/api/classds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classds/:id} : Updates an existing classd.
     *
     * @param id the id of the classd to save.
     * @param classd the classd to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classd,
     * or with status {@code 400 (Bad Request)} if the classd is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classd couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classds/{id}")
    public ResponseEntity<Classd> updateClassd(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Classd classd
    ) throws URISyntaxException {
        log.debug("REST request to update Classd : {}, {}", id, classd);
        if (classd.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classd.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Classd result = classdService.update(classd);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classd.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /classds/:id} : Partial updates given fields of an existing classd, field will ignore if it is null
     *
     * @param id the id of the classd to save.
     * @param classd the classd to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classd,
     * or with status {@code 400 (Bad Request)} if the classd is not valid,
     * or with status {@code 404 (Not Found)} if the classd is not found,
     * or with status {@code 500 (Internal Server Error)} if the classd couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/classds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Classd> partialUpdateClassd(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Classd classd
    ) throws URISyntaxException {
        log.debug("REST request to partial update Classd partially : {}, {}", id, classd);
        if (classd.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classd.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classdRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Classd> result = classdService.partialUpdate(classd);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classd.getId().toString())
        );
    }

    /**
     * {@code GET  /classds} : get all the classds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classds in body.
     */
    @GetMapping("/classds")
    public ResponseEntity<List<Classd>> getAllClassds(ClassdCriteria criteria) {
        log.debug("REST request to get Classds by criteria: {}", criteria);
        List<Classd> entityList = classdQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /classds/count} : count all the classds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/classds/count")
    public ResponseEntity<Long> countClassds(ClassdCriteria criteria) {
        log.debug("REST request to count Classds by criteria: {}", criteria);
        return ResponseEntity.ok().body(classdQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /classds/:id} : get the "id" classd.
     *
     * @param id the id of the classd to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classd, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classds/{id}")
    public ResponseEntity<Classd> getClassd(@PathVariable Long id) {
        log.debug("REST request to get Classd : {}", id);
        Optional<Classd> classd = classdService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classd);
    }

    /**
     * {@code DELETE  /classds/:id} : delete the "id" classd.
     *
     * @param id the id of the classd to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classds/{id}")
    public ResponseEntity<Void> deleteClassd(@PathVariable Long id) {
        log.debug("REST request to delete Classd : {}", id);
        classdService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
