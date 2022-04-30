package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Classd;
import com.mycompany.myapp.domain.Classroom;
import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.domain.Teacher;
import com.mycompany.myapp.repository.CourseRepository;
import com.mycompany.myapp.service.CourseService;
import com.mycompany.myapp.service.criteria.CourseCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CourseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CourseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INTRODUCE = "AAAAAAAAAA";
    private static final String UPDATED_INTRODUCE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CLASS_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLASS_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseRepository courseRepository;

    @Mock
    private CourseRepository courseRepositoryMock;

    @Mock
    private CourseService courseServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseMockMvc;

    private Course course;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity(EntityManager em) {
        Course course = new Course().name(DEFAULT_NAME).introduce(DEFAULT_INTRODUCE).classTime(DEFAULT_CLASS_TIME);
        return course;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course().name(UPDATED_NAME).introduce(UPDATED_INTRODUCE).classTime(UPDATED_CLASS_TIME);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();
        // Create the Course
        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getIntroduce()).isEqualTo(DEFAULT_INTRODUCE);
        assertThat(testCourse.getClassTime()).isEqualTo(DEFAULT_CLASS_TIME);
    }

    @Test
    @Transactional
    void createCourseWithExistingId() throws Exception {
        // Create the Course with an existing ID
        course.setId(1L);

        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setName(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIntroduceIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setIntroduce(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setClassTime(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].introduce").value(hasItem(DEFAULT_INTRODUCE)))
            .andExpect(jsonPath("$.[*].classTime").value(hasItem(DEFAULT_CLASS_TIME.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCoursesWithEagerRelationshipsIsEnabled() throws Exception {
        when(courseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCourseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(courseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCoursesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(courseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCourseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(courseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc
            .perform(get(ENTITY_API_URL_ID, course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.introduce").value(DEFAULT_INTRODUCE))
            .andExpect(jsonPath("$.classTime").value(DEFAULT_CLASS_TIME.toString()));
    }

    @Test
    @Transactional
    void getCoursesByIdFiltering() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        Long id = course.getId();

        defaultCourseShouldBeFound("id.equals=" + id);
        defaultCourseShouldNotBeFound("id.notEquals=" + id);

        defaultCourseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.greaterThan=" + id);

        defaultCourseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCoursesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name equals to DEFAULT_NAME
        defaultCourseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name not equals to DEFAULT_NAME
        defaultCourseShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the courseList where name not equals to UPDATED_NAME
        defaultCourseShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCourseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name is not null
        defaultCourseShouldBeFound("name.specified=true");

        // Get all the courseList where name is null
        defaultCourseShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByNameContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name contains DEFAULT_NAME
        defaultCourseShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the courseList where name contains UPDATED_NAME
        defaultCourseShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name does not contain DEFAULT_NAME
        defaultCourseShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the courseList where name does not contain UPDATED_NAME
        defaultCourseShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByIntroduceIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where introduce equals to DEFAULT_INTRODUCE
        defaultCourseShouldBeFound("introduce.equals=" + DEFAULT_INTRODUCE);

        // Get all the courseList where introduce equals to UPDATED_INTRODUCE
        defaultCourseShouldNotBeFound("introduce.equals=" + UPDATED_INTRODUCE);
    }

    @Test
    @Transactional
    void getAllCoursesByIntroduceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where introduce not equals to DEFAULT_INTRODUCE
        defaultCourseShouldNotBeFound("introduce.notEquals=" + DEFAULT_INTRODUCE);

        // Get all the courseList where introduce not equals to UPDATED_INTRODUCE
        defaultCourseShouldBeFound("introduce.notEquals=" + UPDATED_INTRODUCE);
    }

    @Test
    @Transactional
    void getAllCoursesByIntroduceIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where introduce in DEFAULT_INTRODUCE or UPDATED_INTRODUCE
        defaultCourseShouldBeFound("introduce.in=" + DEFAULT_INTRODUCE + "," + UPDATED_INTRODUCE);

        // Get all the courseList where introduce equals to UPDATED_INTRODUCE
        defaultCourseShouldNotBeFound("introduce.in=" + UPDATED_INTRODUCE);
    }

    @Test
    @Transactional
    void getAllCoursesByIntroduceIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where introduce is not null
        defaultCourseShouldBeFound("introduce.specified=true");

        // Get all the courseList where introduce is null
        defaultCourseShouldNotBeFound("introduce.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByIntroduceContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where introduce contains DEFAULT_INTRODUCE
        defaultCourseShouldBeFound("introduce.contains=" + DEFAULT_INTRODUCE);

        // Get all the courseList where introduce contains UPDATED_INTRODUCE
        defaultCourseShouldNotBeFound("introduce.contains=" + UPDATED_INTRODUCE);
    }

    @Test
    @Transactional
    void getAllCoursesByIntroduceNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where introduce does not contain DEFAULT_INTRODUCE
        defaultCourseShouldNotBeFound("introduce.doesNotContain=" + DEFAULT_INTRODUCE);

        // Get all the courseList where introduce does not contain UPDATED_INTRODUCE
        defaultCourseShouldBeFound("introduce.doesNotContain=" + UPDATED_INTRODUCE);
    }

    @Test
    @Transactional
    void getAllCoursesByClassTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where classTime equals to DEFAULT_CLASS_TIME
        defaultCourseShouldBeFound("classTime.equals=" + DEFAULT_CLASS_TIME);

        // Get all the courseList where classTime equals to UPDATED_CLASS_TIME
        defaultCourseShouldNotBeFound("classTime.equals=" + UPDATED_CLASS_TIME);
    }

    @Test
    @Transactional
    void getAllCoursesByClassTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where classTime not equals to DEFAULT_CLASS_TIME
        defaultCourseShouldNotBeFound("classTime.notEquals=" + DEFAULT_CLASS_TIME);

        // Get all the courseList where classTime not equals to UPDATED_CLASS_TIME
        defaultCourseShouldBeFound("classTime.notEquals=" + UPDATED_CLASS_TIME);
    }

    @Test
    @Transactional
    void getAllCoursesByClassTimeIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where classTime in DEFAULT_CLASS_TIME or UPDATED_CLASS_TIME
        defaultCourseShouldBeFound("classTime.in=" + DEFAULT_CLASS_TIME + "," + UPDATED_CLASS_TIME);

        // Get all the courseList where classTime equals to UPDATED_CLASS_TIME
        defaultCourseShouldNotBeFound("classTime.in=" + UPDATED_CLASS_TIME);
    }

    @Test
    @Transactional
    void getAllCoursesByClassTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where classTime is not null
        defaultCourseShouldBeFound("classTime.specified=true");

        // Get all the courseList where classTime is null
        defaultCourseShouldNotBeFound("classTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByClassdIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Classd classd;
        if (TestUtil.findAll(em, Classd.class).isEmpty()) {
            classd = ClassdResourceIT.createEntity(em);
            em.persist(classd);
            em.flush();
        } else {
            classd = TestUtil.findAll(em, Classd.class).get(0);
        }
        em.persist(classd);
        em.flush();
        course.addClassd(classd);
        courseRepository.saveAndFlush(course);
        Long classdId = classd.getId();

        // Get all the courseList where classd equals to classdId
        defaultCourseShouldBeFound("classdId.equals=" + classdId);

        // Get all the courseList where classd equals to (classdId + 1)
        defaultCourseShouldNotBeFound("classdId.equals=" + (classdId + 1));
    }

    @Test
    @Transactional
    void getAllCoursesByClassroomIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Classroom classroom;
        if (TestUtil.findAll(em, Classroom.class).isEmpty()) {
            classroom = ClassroomResourceIT.createEntity(em);
            em.persist(classroom);
            em.flush();
        } else {
            classroom = TestUtil.findAll(em, Classroom.class).get(0);
        }
        em.persist(classroom);
        em.flush();
        course.addClassroom(classroom);
        courseRepository.saveAndFlush(course);
        Long classroomId = classroom.getId();

        // Get all the courseList where classroom equals to classroomId
        defaultCourseShouldBeFound("classroomId.equals=" + classroomId);

        // Get all the courseList where classroom equals to (classroomId + 1)
        defaultCourseShouldNotBeFound("classroomId.equals=" + (classroomId + 1));
    }

    @Test
    @Transactional
    void getAllCoursesByTeacherIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Teacher teacher;
        if (TestUtil.findAll(em, Teacher.class).isEmpty()) {
            teacher = TeacherResourceIT.createEntity(em);
            em.persist(teacher);
            em.flush();
        } else {
            teacher = TestUtil.findAll(em, Teacher.class).get(0);
        }
        em.persist(teacher);
        em.flush();
        course.addTeacher(teacher);
        courseRepository.saveAndFlush(course);
        Long teacherId = teacher.getId();

        // Get all the courseList where teacher equals to teacherId
        defaultCourseShouldBeFound("teacherId.equals=" + teacherId);

        // Get all the courseList where teacher equals to (teacherId + 1)
        defaultCourseShouldNotBeFound("teacherId.equals=" + (teacherId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourseShouldBeFound(String filter) throws Exception {
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].introduce").value(hasItem(DEFAULT_INTRODUCE)))
            .andExpect(jsonPath("$.[*].classTime").value(hasItem(DEFAULT_CLASS_TIME.toString())));

        // Check, that the count call also returns 1
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourseShouldNotBeFound(String filter) throws Exception {
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse.name(UPDATED_NAME).introduce(UPDATED_INTRODUCE).classTime(UPDATED_CLASS_TIME);

        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourse))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getIntroduce()).isEqualTo(UPDATED_INTRODUCE);
        assertThat(testCourse.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
    }

    @Test
    @Transactional
    void putNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, course.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course using partial update
        Course partialUpdatedCourse = new Course();
        partialUpdatedCourse.setId(course.getId());

        partialUpdatedCourse.classTime(UPDATED_CLASS_TIME);

        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourse))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getIntroduce()).isEqualTo(DEFAULT_INTRODUCE);
        assertThat(testCourse.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
    }

    @Test
    @Transactional
    void fullUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course using partial update
        Course partialUpdatedCourse = new Course();
        partialUpdatedCourse.setId(course.getId());

        partialUpdatedCourse.name(UPDATED_NAME).introduce(UPDATED_INTRODUCE).classTime(UPDATED_CLASS_TIME);

        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourse))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getIntroduce()).isEqualTo(UPDATED_INTRODUCE);
        assertThat(testCourse.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, course.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc
            .perform(delete(ENTITY_API_URL_ID, course.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
