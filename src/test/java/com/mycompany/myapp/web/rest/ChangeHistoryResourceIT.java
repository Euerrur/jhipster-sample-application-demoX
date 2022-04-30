package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ChangeHistory;
import com.mycompany.myapp.repository.ChangeHistoryRepository;
import com.mycompany.myapp.service.criteria.ChangeHistoryCriteria;
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
 * Integration tests for the {@link ChangeHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChangeHistoryResourceIT {

    private static final String DEFAULT_DESCRIBE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIBE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/change-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChangeHistoryRepository changeHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChangeHistoryMockMvc;

    private ChangeHistory changeHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChangeHistory createEntity(EntityManager em) {
        ChangeHistory changeHistory = new ChangeHistory().describe(DEFAULT_DESCRIBE);
        return changeHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChangeHistory createUpdatedEntity(EntityManager em) {
        ChangeHistory changeHistory = new ChangeHistory().describe(UPDATED_DESCRIBE);
        return changeHistory;
    }

    @BeforeEach
    public void initTest() {
        changeHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createChangeHistory() throws Exception {
        int databaseSizeBeforeCreate = changeHistoryRepository.findAll().size();
        // Create the ChangeHistory
        restChangeHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(changeHistory)))
            .andExpect(status().isCreated());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ChangeHistory testChangeHistory = changeHistoryList.get(changeHistoryList.size() - 1);
        assertThat(testChangeHistory.getDescribe()).isEqualTo(DEFAULT_DESCRIBE);
    }

    @Test
    @Transactional
    void createChangeHistoryWithExistingId() throws Exception {
        // Create the ChangeHistory with an existing ID
        changeHistory.setId(1L);

        int databaseSizeBeforeCreate = changeHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChangeHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(changeHistory)))
            .andExpect(status().isBadRequest());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescribeIsRequired() throws Exception {
        int databaseSizeBeforeTest = changeHistoryRepository.findAll().size();
        // set the field null
        changeHistory.setDescribe(null);

        // Create the ChangeHistory, which fails.

        restChangeHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(changeHistory)))
            .andExpect(status().isBadRequest());

        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChangeHistories() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        // Get all the changeHistoryList
        restChangeHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(changeHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].describe").value(hasItem(DEFAULT_DESCRIBE)));
    }

    @Test
    @Transactional
    void getChangeHistory() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        // Get the changeHistory
        restChangeHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, changeHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(changeHistory.getId().intValue()))
            .andExpect(jsonPath("$.describe").value(DEFAULT_DESCRIBE));
    }

    @Test
    @Transactional
    void getChangeHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        Long id = changeHistory.getId();

        defaultChangeHistoryShouldBeFound("id.equals=" + id);
        defaultChangeHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultChangeHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChangeHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultChangeHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChangeHistoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChangeHistoriesByDescribeIsEqualToSomething() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        // Get all the changeHistoryList where describe equals to DEFAULT_DESCRIBE
        defaultChangeHistoryShouldBeFound("describe.equals=" + DEFAULT_DESCRIBE);

        // Get all the changeHistoryList where describe equals to UPDATED_DESCRIBE
        defaultChangeHistoryShouldNotBeFound("describe.equals=" + UPDATED_DESCRIBE);
    }

    @Test
    @Transactional
    void getAllChangeHistoriesByDescribeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        // Get all the changeHistoryList where describe not equals to DEFAULT_DESCRIBE
        defaultChangeHistoryShouldNotBeFound("describe.notEquals=" + DEFAULT_DESCRIBE);

        // Get all the changeHistoryList where describe not equals to UPDATED_DESCRIBE
        defaultChangeHistoryShouldBeFound("describe.notEquals=" + UPDATED_DESCRIBE);
    }

    @Test
    @Transactional
    void getAllChangeHistoriesByDescribeIsInShouldWork() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        // Get all the changeHistoryList where describe in DEFAULT_DESCRIBE or UPDATED_DESCRIBE
        defaultChangeHistoryShouldBeFound("describe.in=" + DEFAULT_DESCRIBE + "," + UPDATED_DESCRIBE);

        // Get all the changeHistoryList where describe equals to UPDATED_DESCRIBE
        defaultChangeHistoryShouldNotBeFound("describe.in=" + UPDATED_DESCRIBE);
    }

    @Test
    @Transactional
    void getAllChangeHistoriesByDescribeIsNullOrNotNull() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        // Get all the changeHistoryList where describe is not null
        defaultChangeHistoryShouldBeFound("describe.specified=true");

        // Get all the changeHistoryList where describe is null
        defaultChangeHistoryShouldNotBeFound("describe.specified=false");
    }

    @Test
    @Transactional
    void getAllChangeHistoriesByDescribeContainsSomething() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        // Get all the changeHistoryList where describe contains DEFAULT_DESCRIBE
        defaultChangeHistoryShouldBeFound("describe.contains=" + DEFAULT_DESCRIBE);

        // Get all the changeHistoryList where describe contains UPDATED_DESCRIBE
        defaultChangeHistoryShouldNotBeFound("describe.contains=" + UPDATED_DESCRIBE);
    }

    @Test
    @Transactional
    void getAllChangeHistoriesByDescribeNotContainsSomething() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        // Get all the changeHistoryList where describe does not contain DEFAULT_DESCRIBE
        defaultChangeHistoryShouldNotBeFound("describe.doesNotContain=" + DEFAULT_DESCRIBE);

        // Get all the changeHistoryList where describe does not contain UPDATED_DESCRIBE
        defaultChangeHistoryShouldBeFound("describe.doesNotContain=" + UPDATED_DESCRIBE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChangeHistoryShouldBeFound(String filter) throws Exception {
        restChangeHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(changeHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].describe").value(hasItem(DEFAULT_DESCRIBE)));

        // Check, that the count call also returns 1
        restChangeHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChangeHistoryShouldNotBeFound(String filter) throws Exception {
        restChangeHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChangeHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChangeHistory() throws Exception {
        // Get the changeHistory
        restChangeHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChangeHistory() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        int databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size();

        // Update the changeHistory
        ChangeHistory updatedChangeHistory = changeHistoryRepository.findById(changeHistory.getId()).get();
        // Disconnect from session so that the updates on updatedChangeHistory are not directly saved in db
        em.detach(updatedChangeHistory);
        updatedChangeHistory.describe(UPDATED_DESCRIBE);

        restChangeHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChangeHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChangeHistory))
            )
            .andExpect(status().isOk());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate);
        ChangeHistory testChangeHistory = changeHistoryList.get(changeHistoryList.size() - 1);
        assertThat(testChangeHistory.getDescribe()).isEqualTo(UPDATED_DESCRIBE);
    }

    @Test
    @Transactional
    void putNonExistingChangeHistory() throws Exception {
        int databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size();
        changeHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChangeHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, changeHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(changeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChangeHistory() throws Exception {
        int databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size();
        changeHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChangeHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(changeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChangeHistory() throws Exception {
        int databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size();
        changeHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChangeHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(changeHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChangeHistoryWithPatch() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        int databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size();

        // Update the changeHistory using partial update
        ChangeHistory partialUpdatedChangeHistory = new ChangeHistory();
        partialUpdatedChangeHistory.setId(changeHistory.getId());

        restChangeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChangeHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChangeHistory))
            )
            .andExpect(status().isOk());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate);
        ChangeHistory testChangeHistory = changeHistoryList.get(changeHistoryList.size() - 1);
        assertThat(testChangeHistory.getDescribe()).isEqualTo(DEFAULT_DESCRIBE);
    }

    @Test
    @Transactional
    void fullUpdateChangeHistoryWithPatch() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        int databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size();

        // Update the changeHistory using partial update
        ChangeHistory partialUpdatedChangeHistory = new ChangeHistory();
        partialUpdatedChangeHistory.setId(changeHistory.getId());

        partialUpdatedChangeHistory.describe(UPDATED_DESCRIBE);

        restChangeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChangeHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChangeHistory))
            )
            .andExpect(status().isOk());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate);
        ChangeHistory testChangeHistory = changeHistoryList.get(changeHistoryList.size() - 1);
        assertThat(testChangeHistory.getDescribe()).isEqualTo(UPDATED_DESCRIBE);
    }

    @Test
    @Transactional
    void patchNonExistingChangeHistory() throws Exception {
        int databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size();
        changeHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChangeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, changeHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(changeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChangeHistory() throws Exception {
        int databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size();
        changeHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChangeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(changeHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChangeHistory() throws Exception {
        int databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size();
        changeHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChangeHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(changeHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChangeHistory in the database
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChangeHistory() throws Exception {
        // Initialize the database
        changeHistoryRepository.saveAndFlush(changeHistory);

        int databaseSizeBeforeDelete = changeHistoryRepository.findAll().size();

        // Delete the changeHistory
        restChangeHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, changeHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChangeHistory> changeHistoryList = changeHistoryRepository.findAll();
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
