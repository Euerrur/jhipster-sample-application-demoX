package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Classroom;
import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.repository.ClassroomRepository;
import com.mycompany.myapp.service.ClassroomService;
import com.mycompany.myapp.service.criteria.ClassroomCriteria;
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
 * Integration tests for the {@link ClassroomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClassroomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classrooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassroomRepository classroomRepository;

    @Mock
    private ClassroomRepository classroomRepositoryMock;

    @Mock
    private ClassroomService classroomServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassroomMockMvc;

    private Classroom classroom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classroom createEntity(EntityManager em) {
        Classroom classroom = new Classroom().name(DEFAULT_NAME).address(DEFAULT_ADDRESS);
        return classroom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classroom createUpdatedEntity(EntityManager em) {
        Classroom classroom = new Classroom().name(UPDATED_NAME).address(UPDATED_ADDRESS);
        return classroom;
    }

    @BeforeEach
    public void initTest() {
        classroom = createEntity(em);
    }

    @Test
    @Transactional
    void createClassroom() throws Exception {
        int databaseSizeBeforeCreate = classroomRepository.findAll().size();
        // Create the Classroom
        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classroom)))
            .andExpect(status().isCreated());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeCreate + 1);
        Classroom testClassroom = classroomList.get(classroomList.size() - 1);
        assertThat(testClassroom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClassroom.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createClassroomWithExistingId() throws Exception {
        // Create the Classroom with an existing ID
        classroom.setId(1L);

        int databaseSizeBeforeCreate = classroomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classroom)))
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = classroomRepository.findAll().size();
        // set the field null
        classroom.setName(null);

        // Create the Classroom, which fails.

        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classroom)))
            .andExpect(status().isBadRequest());

        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = classroomRepository.findAll().size();
        // set the field null
        classroom.setAddress(null);

        // Create the Classroom, which fails.

        restClassroomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classroom)))
            .andExpect(status().isBadRequest());

        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassrooms() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList
        restClassroomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassroomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(classroomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClassroomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(classroomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassroomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(classroomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClassroomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(classroomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getClassroom() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get the classroom
        restClassroomMockMvc
            .perform(get(ENTITY_API_URL_ID, classroom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classroom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getClassroomsByIdFiltering() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        Long id = classroom.getId();

        defaultClassroomShouldBeFound("id.equals=" + id);
        defaultClassroomShouldNotBeFound("id.notEquals=" + id);

        defaultClassroomShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassroomShouldNotBeFound("id.greaterThan=" + id);

        defaultClassroomShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassroomShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassroomsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where name equals to DEFAULT_NAME
        defaultClassroomShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the classroomList where name equals to UPDATED_NAME
        defaultClassroomShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassroomsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where name not equals to DEFAULT_NAME
        defaultClassroomShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the classroomList where name not equals to UPDATED_NAME
        defaultClassroomShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassroomsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where name in DEFAULT_NAME or UPDATED_NAME
        defaultClassroomShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the classroomList where name equals to UPDATED_NAME
        defaultClassroomShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassroomsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where name is not null
        defaultClassroomShouldBeFound("name.specified=true");

        // Get all the classroomList where name is null
        defaultClassroomShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllClassroomsByNameContainsSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where name contains DEFAULT_NAME
        defaultClassroomShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the classroomList where name contains UPDATED_NAME
        defaultClassroomShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassroomsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where name does not contain DEFAULT_NAME
        defaultClassroomShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the classroomList where name does not contain UPDATED_NAME
        defaultClassroomShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassroomsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where address equals to DEFAULT_ADDRESS
        defaultClassroomShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the classroomList where address equals to UPDATED_ADDRESS
        defaultClassroomShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClassroomsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where address not equals to DEFAULT_ADDRESS
        defaultClassroomShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the classroomList where address not equals to UPDATED_ADDRESS
        defaultClassroomShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClassroomsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultClassroomShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the classroomList where address equals to UPDATED_ADDRESS
        defaultClassroomShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClassroomsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where address is not null
        defaultClassroomShouldBeFound("address.specified=true");

        // Get all the classroomList where address is null
        defaultClassroomShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllClassroomsByAddressContainsSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where address contains DEFAULT_ADDRESS
        defaultClassroomShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the classroomList where address contains UPDATED_ADDRESS
        defaultClassroomShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClassroomsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classroomList where address does not contain DEFAULT_ADDRESS
        defaultClassroomShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the classroomList where address does not contain UPDATED_ADDRESS
        defaultClassroomShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClassroomsByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);
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
        classroom.addCourse(course);
        classroomRepository.saveAndFlush(classroom);
        Long courseId = course.getId();

        // Get all the classroomList where course equals to courseId
        defaultClassroomShouldBeFound("courseId.equals=" + courseId);

        // Get all the classroomList where course equals to (courseId + 1)
        defaultClassroomShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassroomShouldBeFound(String filter) throws Exception {
        restClassroomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classroom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restClassroomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassroomShouldNotBeFound(String filter) throws Exception {
        restClassroomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassroomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassroom() throws Exception {
        // Get the classroom
        restClassroomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassroom() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();

        // Update the classroom
        Classroom updatedClassroom = classroomRepository.findById(classroom.getId()).get();
        // Disconnect from session so that the updates on updatedClassroom are not directly saved in db
        em.detach(updatedClassroom);
        updatedClassroom.name(UPDATED_NAME).address(UPDATED_ADDRESS);

        restClassroomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClassroom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClassroom))
            )
            .andExpect(status().isOk());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
        Classroom testClassroom = classroomList.get(classroomList.size() - 1);
        assertThat(testClassroom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClassroom.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classroom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classroom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classroom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classroom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassroomWithPatch() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();

        // Update the classroom using partial update
        Classroom partialUpdatedClassroom = new Classroom();
        partialUpdatedClassroom.setId(classroom.getId());

        partialUpdatedClassroom.name(UPDATED_NAME).address(UPDATED_ADDRESS);

        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassroom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassroom))
            )
            .andExpect(status().isOk());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
        Classroom testClassroom = classroomList.get(classroomList.size() - 1);
        assertThat(testClassroom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClassroom.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateClassroomWithPatch() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();

        // Update the classroom using partial update
        Classroom partialUpdatedClassroom = new Classroom();
        partialUpdatedClassroom.setId(classroom.getId());

        partialUpdatedClassroom.name(UPDATED_NAME).address(UPDATED_ADDRESS);

        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassroom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassroom))
            )
            .andExpect(status().isOk());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
        Classroom testClassroom = classroomList.get(classroomList.size() - 1);
        assertThat(testClassroom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClassroom.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classroom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classroom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classroom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassroom() throws Exception {
        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();
        classroom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassroomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(classroom))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classroom in the database
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassroom() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        int databaseSizeBeforeDelete = classroomRepository.findAll().size();

        // Delete the classroom
        restClassroomMockMvc
            .perform(delete(ENTITY_API_URL_ID, classroom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Classroom> classroomList = classroomRepository.findAll();
        assertThat(classroomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
