package ru.pelmegov.euroshini.web.rest;

import ru.pelmegov.euroshini.EuroshiniApp;

import ru.pelmegov.euroshini.domain.Tire;
import ru.pelmegov.euroshini.repository.TireRepository;
import ru.pelmegov.euroshini.repository.search.TireSearchRepository;
import ru.pelmegov.euroshini.service.dto.TireDTO;
import ru.pelmegov.euroshini.service.mapper.TireMapper;
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

import ru.pelmegov.euroshini.domain.enumeration.Technology;
import ru.pelmegov.euroshini.domain.enumeration.Season;
import ru.pelmegov.euroshini.domain.enumeration.Manufacturer;
/**
 * Test class for the TireResource REST controller.
 *
 * @see TireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EuroshiniApp.class)
public class TireResourceIntTest {

    private static final String DEFAULT_MARK = "AAAAAAAAAA";
    private static final String UPDATED_MARK = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final Double DEFAULT_RADIUS = 1D;
    private static final Double UPDATED_RADIUS = 2D;

    private static final String DEFAULT_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_SIZE = "BBBBBBBBBB";

    private static final Technology DEFAULT_TECHNOLOGY = Technology.DEFAULT;
    private static final Technology UPDATED_TECHNOLOGY = Technology.RUNFLAT;

    private static final String DEFAULT_INDEX = "AAAAAAAAAA";
    private static final String UPDATED_INDEX = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_YEAR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_STRONG = false;
    private static final Boolean UPDATED_IS_STRONG = true;

    private static final Season DEFAULT_SEASON = Season.WINTER;
    private static final Season UPDATED_SEASON = Season.SUMMER;

    private static final Manufacturer DEFAULT_MANUFACTURER = Manufacturer.EURO;
    private static final Manufacturer UPDATED_MANUFACTURER = Manufacturer.CHINA;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    @Autowired
    private TireRepository tireRepository;

    @Autowired
    private TireMapper tireMapper;

    @Autowired
    private TireSearchRepository tireSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTireMockMvc;

    private Tire tire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TireResource tireResource = new TireResource(tireRepository, tireMapper, tireSearchRepository);
        this.restTireMockMvc = MockMvcBuilders.standaloneSetup(tireResource)
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
    public static Tire createEntity(EntityManager em) {
        Tire tire = new Tire()
            .mark(DEFAULT_MARK)
            .model(DEFAULT_MODEL)
            .radius(DEFAULT_RADIUS)
            .size(DEFAULT_SIZE)
            .technology(DEFAULT_TECHNOLOGY)
            .index(DEFAULT_INDEX)
            .releaseYear(DEFAULT_RELEASE_YEAR)
            .isStrong(DEFAULT_IS_STRONG)
            .season(DEFAULT_SEASON)
            .manufacturer(DEFAULT_MANUFACTURER)
            .price(DEFAULT_PRICE)
            .count(DEFAULT_COUNT);
        return tire;
    }

    @Before
    public void initTest() {
        tireSearchRepository.deleteAll();
        tire = createEntity(em);
    }

    @Test
    @Transactional
    public void createTire() throws Exception {
        int databaseSizeBeforeCreate = tireRepository.findAll().size();

        // Create the Tire
        TireDTO tireDTO = tireMapper.toDto(tire);
        restTireMockMvc.perform(post("/api/tires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tireDTO)))
            .andExpect(status().isCreated());

        // Validate the Tire in the database
        List<Tire> tireList = tireRepository.findAll();
        assertThat(tireList).hasSize(databaseSizeBeforeCreate + 1);
        Tire testTire = tireList.get(tireList.size() - 1);
        assertThat(testTire.getMark()).isEqualTo(DEFAULT_MARK);
        assertThat(testTire.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testTire.getRadius()).isEqualTo(DEFAULT_RADIUS);
        assertThat(testTire.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testTire.getTechnology()).isEqualTo(DEFAULT_TECHNOLOGY);
        assertThat(testTire.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testTire.getReleaseYear()).isEqualTo(DEFAULT_RELEASE_YEAR);
        assertThat(testTire.isIsStrong()).isEqualTo(DEFAULT_IS_STRONG);
        assertThat(testTire.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testTire.getManufacturer()).isEqualTo(DEFAULT_MANUFACTURER);
        assertThat(testTire.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testTire.getCount()).isEqualTo(DEFAULT_COUNT);

        // Validate the Tire in Elasticsearch
//        Tire tireEs = tireSearchRepository.findOne(testTire.getId());
//        assertThat(tireEs).isEqualToIgnoringGivenFields(testTire);
    }

    @Test
    @Transactional
    public void createTireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tireRepository.findAll().size();

        // Create the Tire with an existing ID
        tire.setId(1L);
        TireDTO tireDTO = tireMapper.toDto(tire);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTireMockMvc.perform(post("/api/tires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tire in the database
        List<Tire> tireList = tireRepository.findAll();
        assertThat(tireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTires() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList
        restTireMockMvc.perform(get("/api/tires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tire.getId().intValue())))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].radius").value(hasItem(DEFAULT_RADIUS.doubleValue())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())))
            .andExpect(jsonPath("$.[*].technology").value(hasItem(DEFAULT_TECHNOLOGY.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX.toString())))
            .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR.toString())))
            .andExpect(jsonPath("$.[*].isStrong").value(hasItem(DEFAULT_IS_STRONG.booleanValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON.toString())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)));
    }

    @Test
    @Transactional
    public void getTire() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get the tire
        restTireMockMvc.perform(get("/api/tires/{id}", tire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tire.getId().intValue()))
            .andExpect(jsonPath("$.mark").value(DEFAULT_MARK.toString()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.radius").value(DEFAULT_RADIUS.doubleValue()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.toString()))
            .andExpect(jsonPath("$.technology").value(DEFAULT_TECHNOLOGY.toString()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX.toString()))
            .andExpect(jsonPath("$.releaseYear").value(DEFAULT_RELEASE_YEAR.toString()))
            .andExpect(jsonPath("$.isStrong").value(DEFAULT_IS_STRONG.booleanValue()))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON.toString()))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT));
    }

    @Test
    @Transactional
    public void getNonExistingTire() throws Exception {
        // Get the tire
        restTireMockMvc.perform(get("/api/tires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTire() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);
        tireSearchRepository.save(tire);
        int databaseSizeBeforeUpdate = tireRepository.findAll().size();

        // Update the tire
        Tire updatedTire = tireRepository.findOne(tire.getId());
        // Disconnect from session so that the updates on updatedTire are not directly saved in db
        em.detach(updatedTire);
        updatedTire
            .mark(UPDATED_MARK)
            .model(UPDATED_MODEL)
            .radius(UPDATED_RADIUS)
            .size(UPDATED_SIZE)
            .technology(UPDATED_TECHNOLOGY)
            .index(UPDATED_INDEX)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .isStrong(UPDATED_IS_STRONG)
            .season(UPDATED_SEASON)
            .manufacturer(UPDATED_MANUFACTURER)
            .price(UPDATED_PRICE)
            .count(UPDATED_COUNT);
        TireDTO tireDTO = tireMapper.toDto(updatedTire);

        restTireMockMvc.perform(put("/api/tires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tireDTO)))
            .andExpect(status().isOk());

        // Validate the Tire in the database
        List<Tire> tireList = tireRepository.findAll();
        assertThat(tireList).hasSize(databaseSizeBeforeUpdate);
        Tire testTire = tireList.get(tireList.size() - 1);
        assertThat(testTire.getMark()).isEqualTo(UPDATED_MARK);
        assertThat(testTire.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testTire.getRadius()).isEqualTo(UPDATED_RADIUS);
        assertThat(testTire.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testTire.getTechnology()).isEqualTo(UPDATED_TECHNOLOGY);
        assertThat(testTire.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testTire.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testTire.isIsStrong()).isEqualTo(UPDATED_IS_STRONG);
        assertThat(testTire.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testTire.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
        assertThat(testTire.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTire.getCount()).isEqualTo(UPDATED_COUNT);

        // Validate the Tire in Elasticsearch
//        Tire tireEs = tireSearchRepository.findOne(testTire.getId());
//        assertThat(tireEs).isEqualToIgnoringGivenFields(testTire);
    }

    @Test
    @Transactional
    public void updateNonExistingTire() throws Exception {
        int databaseSizeBeforeUpdate = tireRepository.findAll().size();

        // Create the Tire
        TireDTO tireDTO = tireMapper.toDto(tire);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTireMockMvc.perform(put("/api/tires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tireDTO)))
            .andExpect(status().isCreated());

        // Validate the Tire in the database
        List<Tire> tireList = tireRepository.findAll();
        assertThat(tireList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTire() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);
        tireSearchRepository.save(tire);
        int databaseSizeBeforeDelete = tireRepository.findAll().size();

        // Get the tire
        restTireMockMvc.perform(delete("/api/tires/{id}", tire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tireExistsInEs = tireSearchRepository.exists(tire.getId());
        assertThat(tireExistsInEs).isFalse();

        // Validate the database is empty
        List<Tire> tireList = tireRepository.findAll();
        assertThat(tireList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTire() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);
        tireSearchRepository.save(tire);

        // Search the tire
        restTireMockMvc.perform(get("/api/_search/tires?query=id:" + tire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tire.getId().intValue())))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].radius").value(hasItem(DEFAULT_RADIUS.doubleValue())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())))
            .andExpect(jsonPath("$.[*].technology").value(hasItem(DEFAULT_TECHNOLOGY.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX.toString())))
            .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR.toString())))
            .andExpect(jsonPath("$.[*].isStrong").value(hasItem(DEFAULT_IS_STRONG.booleanValue())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON.toString())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tire.class);
        Tire tire1 = new Tire();
        tire1.setId(1L);
        Tire tire2 = new Tire();
        tire2.setId(tire1.getId());
        assertThat(tire1).isEqualTo(tire2);
        tire2.setId(2L);
        assertThat(tire1).isNotEqualTo(tire2);
        tire1.setId(null);
        assertThat(tire1).isNotEqualTo(tire2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TireDTO.class);
        TireDTO tireDTO1 = new TireDTO();
        tireDTO1.setId(1L);
        TireDTO tireDTO2 = new TireDTO();
        assertThat(tireDTO1).isNotEqualTo(tireDTO2);
        tireDTO2.setId(tireDTO1.getId());
        assertThat(tireDTO1).isEqualTo(tireDTO2);
        tireDTO2.setId(2L);
        assertThat(tireDTO1).isNotEqualTo(tireDTO2);
        tireDTO1.setId(null);
        assertThat(tireDTO1).isNotEqualTo(tireDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tireMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tireMapper.fromId(null)).isNull();
    }
}
