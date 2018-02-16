package ru.pelmegov.euroshini.web.rest;

import ru.pelmegov.euroshini.EuroshiniApp;

import ru.pelmegov.euroshini.domain.RevenueHistory;
import ru.pelmegov.euroshini.repository.RevenueHistoryRepository;
import ru.pelmegov.euroshini.repository.search.RevenueHistorySearchRepository;
import ru.pelmegov.euroshini.service.dto.RevenueHistoryDTO;
import ru.pelmegov.euroshini.service.mapper.RevenueHistoryMapper;
import ru.pelmegov.euroshini.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static ru.pelmegov.euroshini.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RevenueHistoryResource REST controller.
 *
 * @see RevenueHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EuroshiniApp.class)
public class RevenueHistoryResourceIntTest {

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    @Autowired
    private RevenueHistoryRepository revenueHistoryRepository;

    @Autowired
    private RevenueHistoryMapper revenueHistoryMapper;

    @Autowired
    private RevenueHistorySearchRepository revenueHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRevenueHistoryMockMvc;

    private RevenueHistory revenueHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RevenueHistoryResource revenueHistoryResource = new RevenueHistoryResource(revenueHistoryRepository, revenueHistoryMapper, revenueHistorySearchRepository);
        this.restRevenueHistoryMockMvc = MockMvcBuilders.standaloneSetup(revenueHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RevenueHistory createEntity(EntityManager em) {
        RevenueHistory revenueHistory = new RevenueHistory()
            .count(DEFAULT_COUNT);
        return revenueHistory;
    }

    @Before
    public void initTest() {
        revenueHistorySearchRepository.deleteAll();
        revenueHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createRevenueHistory() throws Exception {
        int databaseSizeBeforeCreate = revenueHistoryRepository.findAll().size();

        // Create the RevenueHistory
        RevenueHistoryDTO revenueHistoryDTO = revenueHistoryMapper.toDto(revenueHistory);
        restRevenueHistoryMockMvc.perform(post("/api/revenue-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revenueHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the RevenueHistory in the database
        List<RevenueHistory> revenueHistoryList = revenueHistoryRepository.findAll();
        assertThat(revenueHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        RevenueHistory testRevenueHistory = revenueHistoryList.get(revenueHistoryList.size() - 1);
        assertThat(testRevenueHistory.getCount()).isEqualTo(DEFAULT_COUNT);

        // Validate the RevenueHistory in Elasticsearch
//        RevenueHistory revenueHistoryEs = revenueHistorySearchRepository.findOne(testRevenueHistory.getId());
//        assertThat(revenueHistoryEs).isEqualToIgnoringGivenFields(testRevenueHistory);
    }

    @Test
    @Transactional
    public void createRevenueHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = revenueHistoryRepository.findAll().size();

        // Create the RevenueHistory with an existing ID
        revenueHistory.setId(1L);
        RevenueHistoryDTO revenueHistoryDTO = revenueHistoryMapper.toDto(revenueHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevenueHistoryMockMvc.perform(post("/api/revenue-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revenueHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RevenueHistory in the database
        List<RevenueHistory> revenueHistoryList = revenueHistoryRepository.findAll();
        assertThat(revenueHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRevenueHistories() throws Exception {
        // Initialize the database
        revenueHistoryRepository.saveAndFlush(revenueHistory);

        // Get all the revenueHistoryList
        restRevenueHistoryMockMvc.perform(get("/api/revenue-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revenueHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)));
    }

    @Test
    @Transactional
    public void getRevenueHistory() throws Exception {
        // Initialize the database
        revenueHistoryRepository.saveAndFlush(revenueHistory);

        // Get the revenueHistory
        restRevenueHistoryMockMvc.perform(get("/api/revenue-histories/{id}", revenueHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(revenueHistory.getId().intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT));
    }

    @Test
    @Transactional
    public void getNonExistingRevenueHistory() throws Exception {
        // Get the revenueHistory
        restRevenueHistoryMockMvc.perform(get("/api/revenue-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRevenueHistory() throws Exception {
        // Initialize the database
        revenueHistoryRepository.saveAndFlush(revenueHistory);
        revenueHistorySearchRepository.save(revenueHistory);
        int databaseSizeBeforeUpdate = revenueHistoryRepository.findAll().size();

        // Update the revenueHistory
        RevenueHistory updatedRevenueHistory = revenueHistoryRepository.findOne(revenueHistory.getId());
        // Disconnect from session so that the updates on updatedRevenueHistory are not directly saved in db
        em.detach(updatedRevenueHistory);
        updatedRevenueHistory
            .count(UPDATED_COUNT);
        RevenueHistoryDTO revenueHistoryDTO = revenueHistoryMapper.toDto(updatedRevenueHistory);

        restRevenueHistoryMockMvc.perform(put("/api/revenue-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revenueHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the RevenueHistory in the database
        List<RevenueHistory> revenueHistoryList = revenueHistoryRepository.findAll();
        assertThat(revenueHistoryList).hasSize(databaseSizeBeforeUpdate);
        RevenueHistory testRevenueHistory = revenueHistoryList.get(revenueHistoryList.size() - 1);
        assertThat(testRevenueHistory.getCount()).isEqualTo(UPDATED_COUNT);

        // Validate the RevenueHistory in Elasticsearch
//        RevenueHistory revenueHistoryEs = revenueHistorySearchRepository.findOne(testRevenueHistory.getId());
//        assertThat(revenueHistoryEs).isEqualToIgnoringGivenFields(testRevenueHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingRevenueHistory() throws Exception {
        int databaseSizeBeforeUpdate = revenueHistoryRepository.findAll().size();

        // Create the RevenueHistory
        RevenueHistoryDTO revenueHistoryDTO = revenueHistoryMapper.toDto(revenueHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRevenueHistoryMockMvc.perform(put("/api/revenue-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revenueHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the RevenueHistory in the database
        List<RevenueHistory> revenueHistoryList = revenueHistoryRepository.findAll();
        assertThat(revenueHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRevenueHistory() throws Exception {
        // Initialize the database
        revenueHistoryRepository.saveAndFlush(revenueHistory);
        revenueHistorySearchRepository.save(revenueHistory);
        int databaseSizeBeforeDelete = revenueHistoryRepository.findAll().size();

        // Get the revenueHistory
        restRevenueHistoryMockMvc.perform(delete("/api/revenue-histories/{id}", revenueHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean revenueHistoryExistsInEs = revenueHistorySearchRepository.exists(revenueHistory.getId());
        assertThat(revenueHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<RevenueHistory> revenueHistoryList = revenueHistoryRepository.findAll();
        assertThat(revenueHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRevenueHistory() throws Exception {
        // Initialize the database
        revenueHistoryRepository.saveAndFlush(revenueHistory);
        revenueHistorySearchRepository.save(revenueHistory);

        // Search the revenueHistory
        restRevenueHistoryMockMvc.perform(get("/api/_search/revenue-histories?query=id:" + revenueHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revenueHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevenueHistory.class);
        RevenueHistory revenueHistory1 = new RevenueHistory();
        revenueHistory1.setId(1L);
        RevenueHistory revenueHistory2 = new RevenueHistory();
        revenueHistory2.setId(revenueHistory1.getId());
        assertThat(revenueHistory1).isEqualTo(revenueHistory2);
        revenueHistory2.setId(2L);
        assertThat(revenueHistory1).isNotEqualTo(revenueHistory2);
        revenueHistory1.setId(null);
        assertThat(revenueHistory1).isNotEqualTo(revenueHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevenueHistoryDTO.class);
        RevenueHistoryDTO revenueHistoryDTO1 = new RevenueHistoryDTO();
        revenueHistoryDTO1.setId(1L);
        RevenueHistoryDTO revenueHistoryDTO2 = new RevenueHistoryDTO();
        assertThat(revenueHistoryDTO1).isNotEqualTo(revenueHistoryDTO2);
        revenueHistoryDTO2.setId(revenueHistoryDTO1.getId());
        assertThat(revenueHistoryDTO1).isEqualTo(revenueHistoryDTO2);
        revenueHistoryDTO2.setId(2L);
        assertThat(revenueHistoryDTO1).isNotEqualTo(revenueHistoryDTO2);
        revenueHistoryDTO1.setId(null);
        assertThat(revenueHistoryDTO1).isNotEqualTo(revenueHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(revenueHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(revenueHistoryMapper.fromId(null)).isNull();
    }
}
