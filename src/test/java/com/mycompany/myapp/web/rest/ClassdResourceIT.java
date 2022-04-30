package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Classd;
import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.repository.ClassdRepository;
import com.mycompany.myapp.service.criteria.ClassdCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClassdResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassdResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassdRepository classdRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassdMockMvc;

    private Classd classd;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classd createEntity(EntityManager em) {
        Classd classd = new Classd().name(DEFAULT_NAME);
        return classd;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classd createUpdatedEntity(EntityManager em) {
        Classd classd = new Classd().name(UPDATED_NAME);
        return classd;
    }

    @BeforeEach
    public void initTest() {
        classd = createEntity(em);
    }

    @Test
    @Transactional
    void createClassd() throws Exception {
        int databaseSizeBeforeCreate = classdRepository.findAll().size();
        // Create the Classd
        restClassdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classd)))
            .andExpect(status().isCreated());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeCreate + 1);
        Classd testClassd = classdList.get(classdList.size() - 1);
        assertThat(testClassd.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createClassdWithExistingId() throws Exception {
        // Create the Classd with an existing ID
        classd.setId(1L);

        int databaseSizeBeforeCreate = classdRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classd)))
            .andExpect(status().isBadRequest());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = classdRepository.findAll().size();
        // set the field null
        classd.setName(null);

        // Create the Classd, which fails.

        restClassdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classd)))
            .andExpect(status().isBadRequest());

        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassds() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        // Get all the classdList
        restClassdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classd.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getClassd() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        // Get the classd
        restClassdMockMvc
            .perform(get(ENTITY_API_URL_ID, classd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classd.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getClassdsByIdFiltering() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        Long id = classd.getId();

        defaultClassdShouldBeFound("id.equals=" + id);
        defaultClassdShouldNotBeFound("id.notEquals=" + id);

        defaultClassdShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassdShouldNotBeFound("id.greaterThan=" + id);

        defaultClassdShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassdShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassdsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        // Get all the classdList where name equals to DEFAULT_NAME
        defaultClassdShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the classdList where name equals to UPDATED_NAME
        defaultClassdShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassdsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        // Get all the classdList where name not equals to DEFAULT_NAME
        defaultClassdShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the classdList where name not equals to UPDATED_NAME
        defaultClassdShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassdsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        // Get all the classdList where name in DEFAULT_NAME or UPDATED_NAME
        defaultClassdShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the classdList where name equals to UPDATED_NAME
        defaultClassdShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassdsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        // Get all the classdList where name is not null
        defaultClassdShouldBeFound("name.specified=true");

        // Get all the classdList where name is null
        defaultClassdShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllClassdsByNameContainsSomething() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        // Get all the classdList where name contains DEFAULT_NAME
        defaultClassdShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the classdList where name contains UPDATED_NAME
        defaultClassdShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassdsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        // Get all the classdList where name does not contain DEFAULT_NAME
        defaultClassdShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the classdList where name does not contain UPDATED_NAME
        defaultClassdShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassdsByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);
        Course course;
        if (TestUtil.findAll(em, Course.class).isEmpty()) {
            course = CourseResourceIT.createEntity(em);
            em.persist(course);
            em.flush();
        } else {
            course = TestUtil.findAll(em, Course.class).get(0);
        }
        em.persist(course);
        em.flush();
        classd.addCourse(course);
        classdRepository.saveAndFlush(classd);
        Long courseId = course.getId();

        // Get all the classdList where course equals to courseId
        defaultClassdShouldBeFound("courseId.equals=" + courseId);

        // Get all the classdList where course equals to (courseId + 1)
        defaultClassdShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    @Test
    @Transactional
    void getAllClassdsByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);
        Student student;
        if (TestUtil.findAll(em, Student.class).isEmpty()) {
            student = StudentResourceIT.createEntity(em);
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findAll(em, Student.class).get(0);
        }
        em.persist(student);
        em.flush();
        classd.addStudent(student);
        classdRepository.saveAndFlush(classd);
        Long studentId = student.getId();

        // Get all the classdList where student equals to studentId
        defaultClassdShouldBeFound("studentId.equals=" + studentId);

        // Get all the classdList where student equals to (studentId + 1)
        defaultClassdShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassdShouldBeFound(String filter) throws Exception {
        restClassdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classd.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restClassdMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassdShouldNotBeFound(String filter) throws Exception {
        restClassdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassdMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassd() throws Exception {
        // Get the classd
        restClassdMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassd() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        int databaseSizeBeforeUpdate = classdRepository.findAll().size();

        // Update the classd
        Classd updatedClassd = classdRepository.findById(classd.getId()).get();
        // Disconnect from session so that the updates on updatedClassd are not directly saved in db
        em.detach(updatedClassd);
        updatedClassd.name(UPDATED_NAME);

        restClassdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClassd.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClassd))
            )
            .andExpect(status().isOk());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeUpdate);
        Classd testClassd = classdList.get(classdList.size() - 1);
        assertThat(testClassd.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingClassd() throws Exception {
        int databaseSizeBeforeUpdate = classdRepository.findAll().size();
        classd.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classd.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassd() throws Exception {
        int databaseSizeBeforeUpdate = classdRepository.findAll().size();
        classd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassd() throws Exception {
        int databaseSizeBeforeUpdate = classdRepository.findAll().size();
        classd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassdMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classd)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassdWithPatch() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        int databaseSizeBeforeUpdate = classdRepository.findAll().size();

        // Update the classd using partial update
        Classd partialUpdatedClassd = new Classd();
        partialUpdatedClassd.setId(classd.getId());

        partialUpdatedClassd.name(UPDATED_NAME);

        restClassdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassd))
            )
            .andExpect(status().isOk());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeUpdate);
        Classd testClassd = classdList.get(classdList.size() - 1);
        assertThat(testClassd.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateClassdWithPatch() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        int databaseSizeBeforeUpdate = classdRepository.findAll().size();

        // Update the classd using partial update
        Classd partialUpdatedClassd = new Classd();
        partialUpdatedClassd.setId(classd.getId());

        partialUpdatedClassd.name(UPDATED_NAME);

        restClassdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassd))
            )
            .andExpect(status().isOk());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeUpdate);
        Classd testClassd = classdList.get(classdList.size() - 1);
        assertThat(testClassd.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingClassd() throws Exception {
        int databaseSizeBeforeUpdate = classdRepository.findAll().size();
        classd.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassd() throws Exception {
        int databaseSizeBeforeUpdate = classdRepository.findAll().size();
        classd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classd))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassd() throws Exception {
        int databaseSizeBeforeUpdate = classdRepository.findAll().size();
        classd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassdMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(classd)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classd in the database
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassd() throws Exception {
        // Initialize the database
        classdRepository.saveAndFlush(classd);

        int databaseSizeBeforeDelete = classdRepository.findAll().size();

        // Delete the classd
        restClassdMockMvc
            .perform(delete(ENTITY_API_URL_ID, classd.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Classd> classdList = classdRepository.findAll();
        assertThat(classdList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
