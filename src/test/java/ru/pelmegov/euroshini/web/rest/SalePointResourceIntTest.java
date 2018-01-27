package ru.pelmegov.euroshini.web.rest;

import ru.pelmegov.euroshini.EuroshiniApp;

import ru.pelmegov.euroshini.domain.SalePoint;
import ru.pelmegov.euroshini.repository.SalePointRepository;
import ru.pelmegov.euroshini.repository.search.SalePointSearchRepository;
import ru.pelmegov.euroshini.service.dto.SalePointDTO;
import ru.pelmegov.euroshini.service.mapper.SalePointMapper;
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
 * Test class for the SalePointResource REST controller.
 *
 * @see SalePointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EuroshiniApp.class)
public class SalePointResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SalePointRepository salePointRepository;

    @Autowired
    private SalePointMapper salePointMapper;

    @Autowired
    private SalePointSearchRepository salePointSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalePointMockMvc;

    private SalePoint salePoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalePointResource salePointResource = new SalePointResource(salePointRepository, salePointMapper, salePointSearchRepository);
        this.restSalePointMockMvc = MockMvcBuilders.standaloneSetup(salePointResource)
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
    public static SalePoint createEntity(EntityManager em) {
        SalePoint salePoint = new SalePoint()
            .name(DEFAULT_NAME);
        return salePoint;
    }

    @Before
    public void initTest() {
        salePointSearchRepository.deleteAll();
        salePoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalePoint() throws Exception {
        int databaseSizeBeforeCreate = salePointRepository.findAll().size();

        // Create the SalePoint
        SalePointDTO salePointDTO = salePointMapper.toDto(salePoint);
        restSalePointMockMvc.perform(post("/api/sale-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salePointDTO)))
            .andExpect(status().isCreated());

        // Validate the SalePoint in the database
        List<SalePoint> salePointList = salePointRepository.findAll();
        assertThat(salePointList).hasSize(databaseSizeBeforeCreate + 1);
        SalePoint testSalePoint = salePointList.get(salePointList.size() - 1);
        assertThat(testSalePoint.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the SalePoint in Elasticsearch
        SalePoint salePointEs = salePointSearchRepository.findOne(testSalePoint.getId());
        assertThat(salePointEs).isEqualToIgnoringGivenFields(testSalePoint);
    }

    @Test
    @Transactional
    public void createSalePointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salePointRepository.findAll().size();

        // Create the SalePoint with an existing ID
        salePoint.setId(1L);
        SalePointDTO salePointDTO = salePointMapper.toDto(salePoint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalePointMockMvc.perform(post("/api/sale-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salePointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SalePoint in the database
        List<SalePoint> salePointList = salePointRepository.findAll();
        assertThat(salePointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSalePoints() throws Exception {
        // Initialize the database
        salePointRepository.saveAndFlush(salePoint);

        // Get all the salePointList
        restSalePointMockMvc.perform(get("/api/sale-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salePoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSalePoint() throws Exception {
        // Initialize the database
        salePointRepository.saveAndFlush(salePoint);

        // Get the salePoint
        restSalePointMockMvc.perform(get("/api/sale-points/{id}", salePoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salePoint.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSalePoint() throws Exception {
        // Get the salePoint
        restSalePointMockMvc.perform(get("/api/sale-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalePoint() throws Exception {
        // Initialize the database
        salePointRepository.saveAndFlush(salePoint);
        salePointSearchRepository.save(salePoint);
        int databaseSizeBeforeUpdate = salePointRepository.findAll().size();

        // Update the salePoint
        SalePoint updatedSalePoint = salePointRepository.findOne(salePoint.getId());
        // Disconnect from session so that the updates on updatedSalePoint are not directly saved in db
        em.detach(updatedSalePoint);
        updatedSalePoint
            .name(UPDATED_NAME);
        SalePointDTO salePointDTO = salePointMapper.toDto(updatedSalePoint);

        restSalePointMockMvc.perform(put("/api/sale-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salePointDTO)))
            .andExpect(status().isOk());

        // Validate the SalePoint in the database
        List<SalePoint> salePointList = salePointRepository.findAll();
        assertThat(salePointList).hasSize(databaseSizeBeforeUpdate);
        SalePoint testSalePoint = salePointList.get(salePointList.size() - 1);
        assertThat(testSalePoint.getName()).isEqualTo(UPDATED_NAME);

        // Validate the SalePoint in Elasticsearch
        SalePoint salePointEs = salePointSearchRepository.findOne(testSalePoint.getId());
        assertThat(salePointEs).isEqualToIgnoringGivenFields(testSalePoint);
    }

    @Test
    @Transactional
    public void updateNonExistingSalePoint() throws Exception {
        int databaseSizeBeforeUpdate = salePointRepository.findAll().size();

        // Create the SalePoint
        SalePointDTO salePointDTO = salePointMapper.toDto(salePoint);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalePointMockMvc.perform(put("/api/sale-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salePointDTO)))
            .andExpect(status().isCreated());

        // Validate the SalePoint in the database
        List<SalePoint> salePointList = salePointRepository.findAll();
        assertThat(salePointList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void searchSalePoint() throws Exception {
        // Initialize the database
        salePointRepository.saveAndFlush(salePoint);
        salePointSearchRepository.save(salePoint);

        // Search the salePoint
        restSalePointMockMvc.perform(get("/api/_search/sale-points?query=id:" + salePoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salePoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalePoint.class);
        SalePoint salePoint1 = new SalePoint();
        salePoint1.setId(1L);
        SalePoint salePoint2 = new SalePoint();
        salePoint2.setId(salePoint1.getId());
        assertThat(salePoint1).isEqualTo(salePoint2);
        salePoint2.setId(2L);
        assertThat(salePoint1).isNotEqualTo(salePoint2);
        salePoint1.setId(null);
        assertThat(salePoint1).isNotEqualTo(salePoint2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalePointDTO.class);
        SalePointDTO salePointDTO1 = new SalePointDTO();
        salePointDTO1.setId(1L);
        SalePointDTO salePointDTO2 = new SalePointDTO();
        assertThat(salePointDTO1).isNotEqualTo(salePointDTO2);
        salePointDTO2.setId(salePointDTO1.getId());
        assertThat(salePointDTO1).isEqualTo(salePointDTO2);
        salePointDTO2.setId(2L);
        assertThat(salePointDTO1).isNotEqualTo(salePointDTO2);
        salePointDTO1.setId(null);
        assertThat(salePointDTO1).isNotEqualTo(salePointDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(salePointMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(salePointMapper.fromId(null)).isNull();
    }
}
