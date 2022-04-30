package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Teacher;
import com.mycompany.myapp.repository.TeacherRepository;
import com.mycompany.myapp.service.TeacherQueryService;
import com.mycompany.myapp.service.TeacherService;
import com.mycompany.myapp.service.criteria.TeacherCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Teacher}.
 */
@RestController
@RequestMapping("/api")
public class TeacherResource {

    private final Logger log = LoggerFactory.getLogger(TeacherResource.class);

    private static final String ENTITY_NAME = "teacher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeacherService teacherService;

    private final TeacherRepository teacherRepository;

    private final TeacherQueryService teacherQueryService;

    public TeacherResource(TeacherService teacherService, TeacherRepository teacherRepository, TeacherQueryService teacherQueryService) {
        this.teacherService = teacherService;
        this.teacherRepository = teacherRepository;
        this.teacherQueryService = teacherQueryService;
    }

    /**
     * {@code POST  /teachers} : Create a new teacher.
     *
     * @param teacher the teacher to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teacher, or with status {@code 400 (Bad Request)} if the teacher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teachers")
    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody Teacher teacher) throws URISyntaxException {
        log.debug("REST request to save Teacher : {}", teacher);
        if (teacher.getId() != null) {
            throw new BadRequestAlertException("A new teacher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Teacher result = teacherService.save(teacher);
        return ResponseEntity
            .created(new URI("/api/teachers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /teachers/:id} : Updates an existing teacher.
     *
     * @param id the id of the teacher to save.
     * @param teacher the teacher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teacher,
     * or with status {@code 400 (Bad Request)} if the teacher is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teacher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teachers/{id}")
    public ResponseEntity<Teacher> updateTeacher(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Teacher teacher
    ) throws URISyntaxException {
        log.debug("REST request to update Teacher : {}, {}", id, teacher);
        if (teacher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teacher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teacherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Teacher result = teacherService.update(teacher);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teacher.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /teachers/:id} : Partial updates given fields of an existing teacher, field will ignore if it is null
     *
     * @param id the id of the teacher to save.
     * @param teacher the teacher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teacher,
     * or with status {@code 400 (Bad Request)} if the teacher is not valid,
     * or with status {@code 404 (Not Found)} if the teacher is not found,
     * or with status {@code 500 (Internal Server Error)} if the teacher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/teachers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Teacher> partialUpdateTeacher(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Teacher teacher
    ) throws URISyntaxException {
        log.debug("REST request to partial update Teacher partially : {}, {}", id, teacher);
        if (teacher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teacher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teacherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Teacher> result = teacherService.partialUpdate(teacher);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teacher.getId().toString())
        );
    }

    /**
     * {@code GET  /teachers} : get all the teachers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teachers in body.
     */
    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> getAllTeachers(TeacherCriteria criteria) {
        log.debug("REST request to get Teachers by criteria: {}", criteria);
        List<Teacher> entityList = teacherQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /teachers/count} : count all the teachers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/teachers/count")
    public ResponseEntity<Long> countTeachers(TeacherCriteria criteria) {
        log.debug("REST request to count Teachers by criteria: {}", criteria);
        return ResponseEntity.ok().body(teacherQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /teachers/:id} : get the "id" teacher.
     *
     * @param id the id of the teacher to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teacher, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teachers/{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable Long id) {
        log.debug("REST request to get Teacher : {}", id);
        Optional<Teacher> teacher = teacherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teacher);
    }

    /**
     * {@code DELETE  /teachers/:id} : delete the "id" teacher.
     *
     * @param id the id of the teacher to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        log.debug("REST request to delete Teacher : {}", id);
        teacherService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
