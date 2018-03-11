package ru.pelmegov.euroshini.web.rest;

import ru.pelmegov.euroshini.EuroshiniApp;

import ru.pelmegov.euroshini.domain.Tire;
import ru.pelmegov.euroshini.domain.SalePoint;
import ru.pelmegov.euroshini.repository.TireRepository;
import ru.pelmegov.euroshini.service.TireService;
import ru.pelmegov.euroshini.repository.search.TireSearchRepository;
import ru.pelmegov.euroshini.service.dto.TireDTO;
import ru.pelmegov.euroshini.service.mapper.TireMapper;
import ru.pelmegov.euroshini.web.rest.errors.ExceptionTranslator;
import ru.pelmegov.euroshini.service.TireQueryService;

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

    private static final String[] ignoringField = {"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"};

    @Autowired
    private TireRepository tireRepository;

    @Autowired
    private TireMapper tireMapper;

    @Autowired
    private TireService tireService;

    @Autowired
    private TireSearchRepository tireSearchRepository;

    @Autowired
    private TireQueryService tireQueryService;

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
        final TireResource tireResource = new TireResource(tireService, tireQueryService);
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
        Tire tireEs = tireSearchRepository.findOne(testTire.getId());
        assertThat(tireEs).isEqualToIgnoringGivenFields(testTire, ignoringField);
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
    public void getAllTiresByMarkIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where mark equals to DEFAULT_MARK
        defaultTireShouldBeFound("mark.equals=" + DEFAULT_MARK);

        // Get all the tireList where mark equals to UPDATED_MARK
        defaultTireShouldNotBeFound("mark.equals=" + UPDATED_MARK);
    }

    @Test
    @Transactional
    public void getAllTiresByMarkIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where mark in DEFAULT_MARK or UPDATED_MARK
        defaultTireShouldBeFound("mark.in=" + DEFAULT_MARK + "," + UPDATED_MARK);

        // Get all the tireList where mark equals to UPDATED_MARK
        defaultTireShouldNotBeFound("mark.in=" + UPDATED_MARK);
    }

    @Test
    @Transactional
    public void getAllTiresByMarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where mark is not null
        defaultTireShouldBeFound("mark.specified=true");

        // Get all the tireList where mark is null
        defaultTireShouldNotBeFound("mark.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where model equals to DEFAULT_MODEL
        defaultTireShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the tireList where model equals to UPDATED_MODEL
        defaultTireShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllTiresByModelIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultTireShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the tireList where model equals to UPDATED_MODEL
        defaultTireShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllTiresByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where model is not null
        defaultTireShouldBeFound("model.specified=true");

        // Get all the tireList where model is null
        defaultTireShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresByRadiusIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where radius equals to DEFAULT_RADIUS
        defaultTireShouldBeFound("radius.equals=" + DEFAULT_RADIUS);

        // Get all the tireList where radius equals to UPDATED_RADIUS
        defaultTireShouldNotBeFound("radius.equals=" + UPDATED_RADIUS);
    }

    @Test
    @Transactional
    public void getAllTiresByRadiusIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where radius in DEFAULT_RADIUS or UPDATED_RADIUS
        defaultTireShouldBeFound("radius.in=" + DEFAULT_RADIUS + "," + UPDATED_RADIUS);

        // Get all the tireList where radius equals to UPDATED_RADIUS
        defaultTireShouldNotBeFound("radius.in=" + UPDATED_RADIUS);
    }

    @Test
    @Transactional
    public void getAllTiresByRadiusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where radius is not null
        defaultTireShouldBeFound("radius.specified=true");

        // Get all the tireList where radius is null
        defaultTireShouldNotBeFound("radius.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresByTechnologyIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where technology equals to DEFAULT_TECHNOLOGY
        defaultTireShouldBeFound("technology.equals=" + DEFAULT_TECHNOLOGY);

        // Get all the tireList where technology equals to UPDATED_TECHNOLOGY
        defaultTireShouldNotBeFound("technology.equals=" + UPDATED_TECHNOLOGY);
    }

    @Test
    @Transactional
    public void getAllTiresByTechnologyIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where technology in DEFAULT_TECHNOLOGY or UPDATED_TECHNOLOGY
        defaultTireShouldBeFound("technology.in=" + DEFAULT_TECHNOLOGY + "," + UPDATED_TECHNOLOGY);

        // Get all the tireList where technology equals to UPDATED_TECHNOLOGY
        defaultTireShouldNotBeFound("technology.in=" + UPDATED_TECHNOLOGY);
    }

    @Test
    @Transactional
    public void getAllTiresByTechnologyIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where technology is not null
        defaultTireShouldBeFound("technology.specified=true");

        // Get all the tireList where technology is null
        defaultTireShouldNotBeFound("technology.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresByIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where index equals to DEFAULT_INDEX
        defaultTireShouldBeFound("index.equals=" + DEFAULT_INDEX);

        // Get all the tireList where index equals to UPDATED_INDEX
        defaultTireShouldNotBeFound("index.equals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllTiresByIndexIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where index in DEFAULT_INDEX or UPDATED_INDEX
        defaultTireShouldBeFound("index.in=" + DEFAULT_INDEX + "," + UPDATED_INDEX);

        // Get all the tireList where index equals to UPDATED_INDEX
        defaultTireShouldNotBeFound("index.in=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    public void getAllTiresByIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where index is not null
        defaultTireShouldBeFound("index.specified=true");

        // Get all the tireList where index is null
        defaultTireShouldNotBeFound("index.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresByReleaseYearIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where releaseYear equals to DEFAULT_RELEASE_YEAR
        defaultTireShouldBeFound("releaseYear.equals=" + DEFAULT_RELEASE_YEAR);

        // Get all the tireList where releaseYear equals to UPDATED_RELEASE_YEAR
        defaultTireShouldNotBeFound("releaseYear.equals=" + UPDATED_RELEASE_YEAR);
    }

    @Test
    @Transactional
    public void getAllTiresByReleaseYearIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where releaseYear in DEFAULT_RELEASE_YEAR or UPDATED_RELEASE_YEAR
        defaultTireShouldBeFound("releaseYear.in=" + DEFAULT_RELEASE_YEAR + "," + UPDATED_RELEASE_YEAR);

        // Get all the tireList where releaseYear equals to UPDATED_RELEASE_YEAR
        defaultTireShouldNotBeFound("releaseYear.in=" + UPDATED_RELEASE_YEAR);
    }

    @Test
    @Transactional
    public void getAllTiresByReleaseYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where releaseYear is not null
        defaultTireShouldBeFound("releaseYear.specified=true");

        // Get all the tireList where releaseYear is null
        defaultTireShouldNotBeFound("releaseYear.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresByIsStrongIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where isStrong equals to DEFAULT_IS_STRONG
        defaultTireShouldBeFound("isStrong.equals=" + DEFAULT_IS_STRONG);

        // Get all the tireList where isStrong equals to UPDATED_IS_STRONG
        defaultTireShouldNotBeFound("isStrong.equals=" + UPDATED_IS_STRONG);
    }

    @Test
    @Transactional
    public void getAllTiresByIsStrongIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where isStrong in DEFAULT_IS_STRONG or UPDATED_IS_STRONG
        defaultTireShouldBeFound("isStrong.in=" + DEFAULT_IS_STRONG + "," + UPDATED_IS_STRONG);

        // Get all the tireList where isStrong equals to UPDATED_IS_STRONG
        defaultTireShouldNotBeFound("isStrong.in=" + UPDATED_IS_STRONG);
    }

    @Test
    @Transactional
    public void getAllTiresByIsStrongIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where isStrong is not null
        defaultTireShouldBeFound("isStrong.specified=true");

        // Get all the tireList where isStrong is null
        defaultTireShouldNotBeFound("isStrong.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresBySeasonIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where season equals to DEFAULT_SEASON
        defaultTireShouldBeFound("season.equals=" + DEFAULT_SEASON);

        // Get all the tireList where season equals to UPDATED_SEASON
        defaultTireShouldNotBeFound("season.equals=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    public void getAllTiresBySeasonIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where season in DEFAULT_SEASON or UPDATED_SEASON
        defaultTireShouldBeFound("season.in=" + DEFAULT_SEASON + "," + UPDATED_SEASON);

        // Get all the tireList where season equals to UPDATED_SEASON
        defaultTireShouldNotBeFound("season.in=" + UPDATED_SEASON);
    }

    @Test
    @Transactional
    public void getAllTiresBySeasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where season is not null
        defaultTireShouldBeFound("season.specified=true");

        // Get all the tireList where season is null
        defaultTireShouldNotBeFound("season.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresByManufacturerIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where manufacturer equals to DEFAULT_MANUFACTURER
        defaultTireShouldBeFound("manufacturer.equals=" + DEFAULT_MANUFACTURER);

        // Get all the tireList where manufacturer equals to UPDATED_MANUFACTURER
        defaultTireShouldNotBeFound("manufacturer.equals=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    public void getAllTiresByManufacturerIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where manufacturer in DEFAULT_MANUFACTURER or UPDATED_MANUFACTURER
        defaultTireShouldBeFound("manufacturer.in=" + DEFAULT_MANUFACTURER + "," + UPDATED_MANUFACTURER);

        // Get all the tireList where manufacturer equals to UPDATED_MANUFACTURER
        defaultTireShouldNotBeFound("manufacturer.in=" + UPDATED_MANUFACTURER);
    }

    @Test
    @Transactional
    public void getAllTiresByManufacturerIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where manufacturer is not null
        defaultTireShouldBeFound("manufacturer.specified=true");

        // Get all the tireList where manufacturer is null
        defaultTireShouldNotBeFound("manufacturer.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where price equals to DEFAULT_PRICE
        defaultTireShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the tireList where price equals to UPDATED_PRICE
        defaultTireShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllTiresByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultTireShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the tireList where price equals to UPDATED_PRICE
        defaultTireShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllTiresByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is not null
        defaultTireShouldBeFound("price.specified=true");

        // Get all the tireList where price is null
        defaultTireShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresByCountIsEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where count equals to DEFAULT_COUNT
        defaultTireShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the tireList where count equals to UPDATED_COUNT
        defaultTireShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllTiresByCountIsInShouldWork() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultTireShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the tireList where count equals to UPDATED_COUNT
        defaultTireShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllTiresByCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where count is not null
        defaultTireShouldBeFound("count.specified=true");

        // Get all the tireList where count is null
        defaultTireShouldNotBeFound("count.specified=false");
    }

    @Test
    @Transactional
    public void getAllTiresByCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where count greater than or equals to DEFAULT_COUNT
        defaultTireShouldBeFound("count.greaterOrEqualThan=" + DEFAULT_COUNT);

        // Get all the tireList where count greater than or equals to UPDATED_COUNT
        defaultTireShouldNotBeFound("count.greaterOrEqualThan=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllTiresByCountIsLessThanSomething() throws Exception {
        // Initialize the database
        tireRepository.saveAndFlush(tire);

        // Get all the tireList where count less than or equals to DEFAULT_COUNT
        defaultTireShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the tireList where count less than or equals to UPDATED_COUNT
        defaultTireShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }


    @Test
    @Transactional
    public void getAllTiresBySalePointIsEqualToSomething() throws Exception {
        // Initialize the database
        SalePoint salePoint = SalePointResourceIntTest.createEntity(em);
        em.persist(salePoint);
        em.flush();
        tire.setSalePoint(salePoint);
        tireRepository.saveAndFlush(tire);
        Long salePointId = salePoint.getId();

        // Get all the tireList where salePoint equals to salePointId
        defaultTireShouldBeFound("salePointId.equals=" + salePointId);

        // Get all the tireList where salePoint equals to salePointId + 1
        defaultTireShouldNotBeFound("salePointId.equals=" + (salePointId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTireShouldBeFound(String filter) throws Exception {
        restTireMockMvc.perform(get("/api/tires?sort=id,desc&" + filter))
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

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTireShouldNotBeFound(String filter) throws Exception {
        restTireMockMvc.perform(get("/api/tires?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
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
        Tire tireEs = tireSearchRepository.findOne(testTire.getId());
        assertThat(tireEs).isEqualToIgnoringGivenFields(testTire, ignoringField);
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
    public void searchTireByRadiusInUrl() throws Exception {
        // Search the tire by String radius and it'is integer
        checkCustomRadius("/api/_search/tires?query=radius:", "9999");
    }

    @Test
    @Transactional
    public void searchTireByRadiusWithoutUseInUrl() throws Exception {
        // Search the tire by String radius and it'is integer
        checkCustomRadius("/api/_search/tires?query=", "9999");
    }

    private void checkCustomRadius(String checkCustomRadiusString, String radius) throws Exception {
        // have custom double radius
        final Double customRadius = new Double(radius);

        tire.setRadius(customRadius);

        // Initialize the database
        tireRepository.saveAndFlush(tire);
        tireSearchRepository.save(tire);


        restTireMockMvc.perform(get(checkCustomRadiusString + radius))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tire.getId().intValue())))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].radius").value(hasItem(customRadius)))
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
    public void searchTireBySize() throws Exception {

        final String customSize = "165/60";

        tire.setSize(customSize);

        // Initialize the database
        tireRepository.saveAndFlush(tire);
        tireSearchRepository.save(tire);

        restTireMockMvc.perform(get("/api/_search/tires?query=" + customSize))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tire.getId().intValue())))
            .andExpect(jsonPath("$.[*].mark").value(hasItem(DEFAULT_MARK.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].radius").value(hasItem(DEFAULT_RADIUS.doubleValue())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(customSize)))
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
