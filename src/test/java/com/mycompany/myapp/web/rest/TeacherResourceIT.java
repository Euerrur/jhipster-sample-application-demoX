package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.domain.Teacher;
import com.mycompany.myapp.repository.TeacherRepository;
import com.mycompany.myapp.service.TeacherService;
import com.mycompany.myapp.service.criteria.TeacherCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TeacherResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TeacherResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final Integer SMALLER_AGE = 1 - 1;

    private static final String DEFAULT_PROFESSIONAL = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSIONAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/teachers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherRepository teacherRepositoryMock;

    @Mock
    private TeacherService teacherServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeacherMockMvc;

    private Teacher teacher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createEntity(EntityManager em) {
        Teacher teacher = new Teacher().name(DEFAULT_NAME).age(DEFAULT_AGE).professional(DEFAULT_PROFESSIONAL);
        return teacher;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createUpdatedEntity(EntityManager em) {
        Teacher teacher = new Teacher().name(UPDATED_NAME).age(UPDATED_AGE).professional(UPDATED_PROFESSIONAL);
        return teacher;
    }

    @BeforeEach
    public void initTest() {
        teacher = createEntity(em);
    }

    @Test
    @Transactional
    void createTeacher() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();
        // Create the Teacher
        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isCreated());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate + 1);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeacher.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testTeacher.getProfessional()).isEqualTo(DEFAULT_PROFESSIONAL);
    }

    @Test
    @Transactional
    void createTeacherWithExistingId() throws Exception {
        // Create the Teacher with an existing ID
        teacher.setId(1L);

        int databaseSizeBeforeCreate = teacherRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setName(null);

        // Create the Teacher, which fails.

        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isBadRequest());

        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setAge(null);

        // Create the Teacher, which fails.

        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isBadRequest());

        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfessionalIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setProfessional(null);

        // Create the Teacher, which fails.

        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isBadRequest());

        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTeachers() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].professional").value(hasItem(DEFAULT_PROFESSIONAL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeachersWithEagerRelationshipsIsEnabled() throws Exception {
        when(teacherServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeacherMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(teacherServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeachersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(teacherServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeacherMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(teacherServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get the teacher
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL_ID, teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teacher.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.professional").value(DEFAULT_PROFESSIONAL));
    }

    @Test
    @Transactional
    void getTeachersByIdFiltering() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        Long id = teacher.getId();

        defaultTeacherShouldBeFound("id.equals=" + id);
        defaultTeacherShouldNotBeFound("id.notEquals=" + id);

        defaultTeacherShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTeacherShouldNotBeFound("id.greaterThan=" + id);

        defaultTeacherShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTeacherShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTeachersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where name equals to DEFAULT_NAME
        defaultTeacherShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the teacherList where name equals to UPDATED_NAME
        defaultTeacherShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTeachersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where name not equals to DEFAULT_NAME
        defaultTeacherShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the teacherList where name not equals to UPDATED_NAME
        defaultTeacherShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTeachersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTeacherShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the teacherList where name equals to UPDATED_NAME
        defaultTeacherShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTeachersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where name is not null
        defaultTeacherShouldBeFound("name.specified=true");

        // Get all the teacherList where name is null
        defaultTeacherShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTeachersByNameContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where name contains DEFAULT_NAME
        defaultTeacherShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the teacherList where name contains UPDATED_NAME
        defaultTeacherShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTeachersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where name does not contain DEFAULT_NAME
        defaultTeacherShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the teacherList where name does not contain UPDATED_NAME
        defaultTeacherShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTeachersByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where age equals to DEFAULT_AGE
        defaultTeacherShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the teacherList where age equals to UPDATED_AGE
        defaultTeacherShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllTeachersByAgeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where age not equals to DEFAULT_AGE
        defaultTeacherShouldNotBeFound("age.notEquals=" + DEFAULT_AGE);

        // Get all the teacherList where age not equals to UPDATED_AGE
        defaultTeacherShouldBeFound("age.notEquals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllTeachersByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where age in DEFAULT_AGE or UPDATED_AGE
        defaultTeacherShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the teacherList where age equals to UPDATED_AGE
        defaultTeacherShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllTeachersByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where age is not null
        defaultTeacherShouldBeFound("age.specified=true");

        // Get all the teacherList where age is null
        defaultTeacherShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    void getAllTeachersByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where age is greater than or equal to DEFAULT_AGE
        defaultTeacherShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the teacherList where age is greater than or equal to UPDATED_AGE
        defaultTeacherShouldNotBeFound("age.greaterThanOrEqual=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllTeachersByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where age is less than or equal to DEFAULT_AGE
        defaultTeacherShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the teacherList where age is less than or equal to SMALLER_AGE
        defaultTeacherShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllTeachersByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where age is less than DEFAULT_AGE
        defaultTeacherShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the teacherList where age is less than UPDATED_AGE
        defaultTeacherShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllTeachersByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where age is greater than DEFAULT_AGE
        defaultTeacherShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the teacherList where age is greater than SMALLER_AGE
        defaultTeacherShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllTeachersByProfessionalIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where professional equals to DEFAULT_PROFESSIONAL
        defaultTeacherShouldBeFound("professional.equals=" + DEFAULT_PROFESSIONAL);

        // Get all the teacherList where professional equals to UPDATED_PROFESSIONAL
        defaultTeacherShouldNotBeFound("professional.equals=" + UPDATED_PROFESSIONAL);
    }

    @Test
    @Transactional
    void getAllTeachersByProfessionalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where professional not equals to DEFAULT_PROFESSIONAL
        defaultTeacherShouldNotBeFound("professional.notEquals=" + DEFAULT_PROFESSIONAL);

        // Get all the teacherList where professional not equals to UPDATED_PROFESSIONAL
        defaultTeacherShouldBeFound("professional.notEquals=" + UPDATED_PROFESSIONAL);
    }

    @Test
    @Transactional
    void getAllTeachersByProfessionalIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where professional in DEFAULT_PROFESSIONAL or UPDATED_PROFESSIONAL
        defaultTeacherShouldBeFound("professional.in=" + DEFAULT_PROFESSIONAL + "," + UPDATED_PROFESSIONAL);

        // Get all the teacherList where professional equals to UPDATED_PROFESSIONAL
        defaultTeacherShouldNotBeFound("professional.in=" + UPDATED_PROFESSIONAL);
    }

    @Test
    @Transactional
    void getAllTeachersByProfessionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where professional is not null
        defaultTeacherShouldBeFound("professional.specified=true");

        // Get all the teacherList where professional is null
        defaultTeacherShouldNotBeFound("professional.specified=false");
    }

    @Test
    @Transactional
    void getAllTeachersByProfessionalContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where professional contains DEFAULT_PROFESSIONAL
        defaultTeacherShouldBeFound("professional.contains=" + DEFAULT_PROFESSIONAL);

        // Get all the teacherList where professional contains UPDATED_PROFESSIONAL
        defaultTeacherShouldNotBeFound("professional.contains=" + UPDATED_PROFESSIONAL);
    }

    @Test
    @Transactional
    void getAllTeachersByProfessionalNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where professional does not contain DEFAULT_PROFESSIONAL
        defaultTeacherShouldNotBeFound("professional.doesNotContain=" + DEFAULT_PROFESSIONAL);

        // Get all the teacherList where professional does not contain UPDATED_PROFESSIONAL
        defaultTeacherShouldBeFound("professional.doesNotContain=" + UPDATED_PROFESSIONAL);
    }

    @Test
    @Transactional
    void getAllTeachersByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
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
        teacher.addCourse(course);
        teacherRepository.saveAndFlush(teacher);
        Long courseId = course.getId();

        // Get all the teacherList where course equals to courseId
        defaultTeacherShouldBeFound("courseId.equals=" + courseId);

        // Get all the teacherList where course equals to (courseId + 1)
        defaultTeacherShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeacherShouldBeFound(String filter) throws Exception {
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].professional").value(hasItem(DEFAULT_PROFESSIONAL)));

        // Check, that the count call also returns 1
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeacherShouldNotBeFound(String filter) throws Exception {
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTeacher() throws Exception {
        // Get the teacher
        restTeacherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher
        Teacher updatedTeacher = teacherRepository.findById(teacher.getId()).get();
        // Disconnect from session so that the updates on updatedTeacher are not directly saved in db
        em.detach(updatedTeacher);
        updatedTeacher.name(UPDATED_NAME).age(UPDATED_AGE).professional(UPDATED_PROFESSIONAL);

        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTeacher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacher.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testTeacher.getProfessional()).isEqualTo(UPDATED_PROFESSIONAL);
    }

    @Test
    @Transactional
    void putNonExistingTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teacher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeacherWithPatch() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher using partial update
        Teacher partialUpdatedTeacher = new Teacher();
        partialUpdatedTeacher.setId(teacher.getId());

        partialUpdatedTeacher.name(UPDATED_NAME).professional(UPDATED_PROFESSIONAL);

        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacher.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testTeacher.getProfessional()).isEqualTo(UPDATED_PROFESSIONAL);
    }

    @Test
    @Transactional
    void fullUpdateTeacherWithPatch() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher using partial update
        Teacher partialUpdatedTeacher = new Teacher();
        partialUpdatedTeacher.setId(teacher.getId());

        partialUpdatedTeacher.name(UPDATED_NAME).age(UPDATED_AGE).professional(UPDATED_PROFESSIONAL);

        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacher.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testTeacher.getProfessional()).isEqualTo(UPDATED_PROFESSIONAL);
    }

    @Test
    @Transactional
    void patchNonExistingTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeDelete = teacherRepository.findAll().size();

        // Delete the teacher
        restTeacherMockMvc
            .perform(delete(ENTITY_API_URL_ID, teacher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
