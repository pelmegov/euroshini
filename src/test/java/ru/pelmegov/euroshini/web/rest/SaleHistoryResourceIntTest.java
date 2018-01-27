package ru.pelmegov.euroshini.web.rest;

import ru.pelmegov.euroshini.EuroshiniApp;

import ru.pelmegov.euroshini.domain.SaleHistory;
import ru.pelmegov.euroshini.repository.SaleHistoryRepository;
import ru.pelmegov.euroshini.repository.search.SaleHistorySearchRepository;
import ru.pelmegov.euroshini.service.dto.SaleHistoryDTO;
import ru.pelmegov.euroshini.service.mapper.SaleHistoryMapper;
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
import java.math.BigDecimal;
import java.util.List;

import static ru.pelmegov.euroshini.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SaleHistoryResource REST controller.
 *
 * @see SaleHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EuroshiniApp.class)
public class SaleHistoryResourceIntTest {

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SUM = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUM = new BigDecimal(2);

    @Autowired
    private SaleHistoryRepository saleHistoryRepository;

    @Autowired
    private SaleHistoryMapper saleHistoryMapper;

    @Autowired
    private SaleHistorySearchRepository saleHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSaleHistoryMockMvc;

    private SaleHistory saleHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaleHistoryResource saleHistoryResource = new SaleHistoryResource(saleHistoryRepository, saleHistoryMapper, saleHistorySearchRepository);
        this.restSaleHistoryMockMvc = MockMvcBuilders.standaloneSetup(saleHistoryResource)
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
    public static SaleHistory createEntity(EntityManager em) {
        SaleHistory saleHistory = new SaleHistory()
            .count(DEFAULT_COUNT)
            .price(DEFAULT_PRICE)
            .sum(DEFAULT_SUM);
        return saleHistory;
    }

    @Before
    public void initTest() {
        saleHistorySearchRepository.deleteAll();
        saleHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaleHistory() throws Exception {
        int databaseSizeBeforeCreate = saleHistoryRepository.findAll().size();

        // Create the SaleHistory
        SaleHistoryDTO saleHistoryDTO = saleHistoryMapper.toDto(saleHistory);
        restSaleHistoryMockMvc.perform(post("/api/sale-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the SaleHistory in the database
        List<SaleHistory> saleHistoryList = saleHistoryRepository.findAll();
        assertThat(saleHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        SaleHistory testSaleHistory = saleHistoryList.get(saleHistoryList.size() - 1);
        assertThat(testSaleHistory.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testSaleHistory.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testSaleHistory.getSum()).isEqualTo(DEFAULT_SUM);

        // Validate the SaleHistory in Elasticsearch
        SaleHistory saleHistoryEs = saleHistorySearchRepository.findOne(testSaleHistory.getId());
        assertThat(saleHistoryEs).isEqualToIgnoringGivenFields(testSaleHistory);
    }

    @Test
    @Transactional
    public void createSaleHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saleHistoryRepository.findAll().size();

        // Create the SaleHistory with an existing ID
        saleHistory.setId(1L);
        SaleHistoryDTO saleHistoryDTO = saleHistoryMapper.toDto(saleHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleHistoryMockMvc.perform(post("/api/sale-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SaleHistory in the database
        List<SaleHistory> saleHistoryList = saleHistoryRepository.findAll();
        assertThat(saleHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSaleHistories() throws Exception {
        // Initialize the database
        saleHistoryRepository.saveAndFlush(saleHistory);

        // Get all the saleHistoryList
        restSaleHistoryMockMvc.perform(get("/api/sale-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].sum").value(hasItem(DEFAULT_SUM.intValue())));
    }

    @Test
    @Transactional
    public void getSaleHistory() throws Exception {
        // Initialize the database
        saleHistoryRepository.saveAndFlush(saleHistory);

        // Get the saleHistory
        restSaleHistoryMockMvc.perform(get("/api/sale-histories/{id}", saleHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saleHistory.getId().intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.sum").value(DEFAULT_SUM.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSaleHistory() throws Exception {
        // Get the saleHistory
        restSaleHistoryMockMvc.perform(get("/api/sale-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaleHistory() throws Exception {
        // Initialize the database
        saleHistoryRepository.saveAndFlush(saleHistory);
        saleHistorySearchRepository.save(saleHistory);
        int databaseSizeBeforeUpdate = saleHistoryRepository.findAll().size();

        // Update the saleHistory
        SaleHistory updatedSaleHistory = saleHistoryRepository.findOne(saleHistory.getId());
        // Disconnect from session so that the updates on updatedSaleHistory are not directly saved in db
        em.detach(updatedSaleHistory);
        updatedSaleHistory
            .count(UPDATED_COUNT)
            .price(UPDATED_PRICE)
            .sum(UPDATED_SUM);
        SaleHistoryDTO saleHistoryDTO = saleHistoryMapper.toDto(updatedSaleHistory);

        restSaleHistoryMockMvc.perform(put("/api/sale-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the SaleHistory in the database
        List<SaleHistory> saleHistoryList = saleHistoryRepository.findAll();
        assertThat(saleHistoryList).hasSize(databaseSizeBeforeUpdate);
        SaleHistory testSaleHistory = saleHistoryList.get(saleHistoryList.size() - 1);
        assertThat(testSaleHistory.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testSaleHistory.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSaleHistory.getSum()).isEqualTo(UPDATED_SUM);

        // Validate the SaleHistory in Elasticsearch
        SaleHistory saleHistoryEs = saleHistorySearchRepository.findOne(testSaleHistory.getId());
        assertThat(saleHistoryEs).isEqualToIgnoringGivenFields(testSaleHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingSaleHistory() throws Exception {
        int databaseSizeBeforeUpdate = saleHistoryRepository.findAll().size();

        // Create the SaleHistory
        SaleHistoryDTO saleHistoryDTO = saleHistoryMapper.toDto(saleHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSaleHistoryMockMvc.perform(put("/api/sale-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the SaleHistory in the database
        List<SaleHistory> saleHistoryList = saleHistoryRepository.findAll();
        assertThat(saleHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSaleHistory() throws Exception {
        // Initialize the database
        saleHistoryRepository.saveAndFlush(saleHistory);
        saleHistorySearchRepository.save(saleHistory);
        int databaseSizeBeforeDelete = saleHistoryRepository.findAll().size();

        // Get the saleHistory
        restSaleHistoryMockMvc.perform(delete("/api/sale-histories/{id}", saleHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean saleHistoryExistsInEs = saleHistorySearchRepository.exists(saleHistory.getId());
        assertThat(saleHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<SaleHistory> saleHistoryList = saleHistoryRepository.findAll();
        assertThat(saleHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSaleHistory() throws Exception {
        // Initialize the database
        saleHistoryRepository.saveAndFlush(saleHistory);
        saleHistorySearchRepository.save(saleHistory);

        // Search the saleHistory
        restSaleHistoryMockMvc.perform(get("/api/_search/sale-histories?query=id:" + saleHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].sum").value(hasItem(DEFAULT_SUM.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleHistory.class);
        SaleHistory saleHistory1 = new SaleHistory();
        saleHistory1.setId(1L);
        SaleHistory saleHistory2 = new SaleHistory();
        saleHistory2.setId(saleHistory1.getId());
        assertThat(saleHistory1).isEqualTo(saleHistory2);
        saleHistory2.setId(2L);
        assertThat(saleHistory1).isNotEqualTo(saleHistory2);
        saleHistory1.setId(null);
        assertThat(saleHistory1).isNotEqualTo(saleHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleHistoryDTO.class);
        SaleHistoryDTO saleHistoryDTO1 = new SaleHistoryDTO();
        saleHistoryDTO1.setId(1L);
        SaleHistoryDTO saleHistoryDTO2 = new SaleHistoryDTO();
        assertThat(saleHistoryDTO1).isNotEqualTo(saleHistoryDTO2);
        saleHistoryDTO2.setId(saleHistoryDTO1.getId());
        assertThat(saleHistoryDTO1).isEqualTo(saleHistoryDTO2);
        saleHistoryDTO2.setId(2L);
        assertThat(saleHistoryDTO1).isNotEqualTo(saleHistoryDTO2);
        saleHistoryDTO1.setId(null);
        assertThat(saleHistoryDTO1).isNotEqualTo(saleHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(saleHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(saleHistoryMapper.fromId(null)).isNull();
    }
}
